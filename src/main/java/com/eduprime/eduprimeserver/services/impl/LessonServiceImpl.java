package com.eduprime.eduprimeserver.services.impl;

import com.eduprime.eduprimeserver.common.HttpStatusCodes;
import com.eduprime.eduprimeserver.common.HttpStatusMessages;
import com.eduprime.eduprimeserver.domains.Course;
import com.eduprime.eduprimeserver.domains.Lesson;
import com.eduprime.eduprimeserver.dtos.BaseResponse;
import com.eduprime.eduprimeserver.dtos.LessonDto;
import com.eduprime.eduprimeserver.dtos.request.LessonRequest;
import com.eduprime.eduprimeserver.dtos.request.PageReq;
import com.eduprime.eduprimeserver.exceptions.BadRequestException;
import com.eduprime.eduprimeserver.exceptions.EntityNotFoundException;
import com.eduprime.eduprimeserver.exceptions.ForbiddenException;
import com.eduprime.eduprimeserver.exceptions.UnauthorizedException;
import com.eduprime.eduprimeserver.repositories.CourseRepository;
import com.eduprime.eduprimeserver.repositories.LessonRepository;
import com.eduprime.eduprimeserver.services.CourseService;
import com.eduprime.eduprimeserver.services.LessonService;
import com.eduprime.eduprimeserver.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    private final StringRedisTemplate stringRedisTemplate;

    private final CourseService courseService;

    private final EntityManager manager;

    private final CourseRepository courseRepository;

    @Value("${cache.ttl.min}")
    private int cacheTtlMin;

    @Value("${cache.ttl.max}")
    private int cacheTtlMax;

    @Override
    public BaseResponse<?> getListLessonByCourseId(String courseId) throws JsonProcessingException {
        Assert.notNull(courseId, "courseId not null!");

        var course = this.courseRepository.getCourseById(courseId).orElse(null);

        if (course == null) {
            throw new EntityNotFoundException("Khóa học không tồn tại!");
        }

        var lessonsFromCache = this.getListLessonByCourseIdFromCache(courseId).orElse(null);

        if (CollectionUtils.isEmpty(lessonsFromCache)) {
            lessonsFromCache = this.lessonRepository.getListLessonByCourseId(courseId).orElse(null);
            if (lessonsFromCache != null) {
                this.saveLessonsToCache(courseId, lessonsFromCache);
            }
        }

        if (lessonsFromCache != null) {
            var lessonsResponse = lessonsFromCache.stream()
                    .map(LessonDto::of)
                    .collect(Collectors.toList());

            return BaseResponse.of(lessonsResponse, HttpStatusCodes.OK, HttpStatusMessages.OK);
        } else {
            return BaseResponse.error(HttpStatusCodes.NOT_FOUND, HttpStatusMessages.NOT_FOUND);
        }
    }

    @Override
    public BaseResponse<?> getLessonById(String lessonId) {
        Assert.notNull(lessonId, "lessonId not null!");

        return BaseResponse.of(LessonDto.of(this.findLessonById(lessonId)), HttpStatusCodes.OK, HttpStatusMessages.OK);
    }

    @Override
    public BaseResponse<LessonRequest> createLesson(LessonRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new UnauthorizedException("Bạn cần đăng nhập để thực hiện chức năng này!");
        }

        boolean hasRoleAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!hasRoleAdmin) {
            throw new ForbiddenException("Bạn không có quyền truy cập chức năng này!");
        }

        Assert.notNull(request, "Object bài học không được bỏ trống !");
        Assert.notNull(request.getName(), "Tên bài học không được bỏ trống !");
        Assert.notNull(request.getTitle(), "Tiêu đề bài học không được bỏ trống !");
        Assert.notNull(request.getCourseId(), "courseId không được bỏ trống !");

        Course course = null;
        if (request.getCourseId() != null) {
            course = this.courseRepository.getCourseById(request.getCourseId()).orElse(null);

            if (course == null) {
                throw new EntityNotFoundException("Khóa học này đang không tồn tại !");
            }
        }

        int courseItemCount = this.lessonRepository.countByCourseId(request.getCourseId());
        int newOrder = courseItemCount + 1;

        var lesson = Lesson.builder()
                .type("LESSON")
                .name(request.getName())
                .title(request.getTitle())
                .description(request.getDescription())
                .content(request.getContent())
                .lessonOrder(newOrder)
                .pathFile("/uploads/images/%s".formatted(request.getImage()))
                .image(request.getImage())
                .course(course)
//                .lessonDetails()
                .build();

        lesson = this.lessonRepository.save(lesson);

        return BaseResponse.of(LessonDto.of(lesson), HttpStatusCodes.CREATED, HttpStatusMessages.CREATED);
    }

    @Override
    public BaseResponse<?> updateLesson(LessonRequest request, String lessonId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new UnauthorizedException("Bạn cần đăng nhập để thực hiện chức năng này!");
        }

        boolean hasRoleAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!hasRoleAdmin) {
            throw new ForbiddenException("Bạn không có quyền truy cập chức năng này!");
        }

        Course course = null;
        if (request.getCourseId() != null) {
            course = this.courseRepository.getCourseById(request.getCourseId()).orElse(null);

            if (course == null) {
                throw new EntityNotFoundException("Khóa học này đang không tồn tại !");
            }
        }

        Assert.notNull(lessonId, "lessonId not null!");
        Assert.notNull(request, "Object bài học không được bỏ trống !");
        Assert.notNull(request.getName(), "Tên bài học không được bỏ trống !");
        Assert.notNull(request.getTitle(), "Tiêu đề bài học không được bỏ trống !");
        Assert.notNull(request.getCourseId(), "courseId không được bỏ trống !");

        var lesson = this.lessonRepository.getLessonById(lessonId).orElse(null);

        if (lesson == null) {
            throw new EntityNotFoundException("Bài học cần update không tồn tại!");
        }

        lesson.setName(request.getName());
        lesson.setTitle(request.getTitle());
        lesson.setDescription(request.getDescription());
        lesson.setContent(request.getContent());
        lesson.setPathFile("/uploads/images/%s".formatted(request.getImage()));
        lesson.setImage(request.getImage());
        lesson.setCourse(course);
//        lesson.setLessonDetails();

        lesson = this.lessonRepository.save(lesson);

        return BaseResponse.of(LessonDto.of(lesson), HttpStatusCodes.OK, HttpStatusMessages.OK);
    }

    @Override
    public Lesson findLessonById(String lessonId) {
        return this.lessonRepository.getLessonById(lessonId).get();
    }

    @Override
    public BaseResponse<?> deleteLesson(String lessonId) {
        return null;
    }

    @Override
    public BaseResponse<Page> paging(PageReq pageRequest) {
        if (pageRequest.getPageIndex() == null) {
            throw new BadRequestException("Request Thiếu PageIndex");
        }
        if (pageRequest.getPageSize() == null) {
            throw new BadRequestException("Request Thiếu PageSize");
        }

        int pageIndex = pageRequest.getPageIndex();
        int pageSize = pageRequest.getPageSize();

        if (pageIndex > 0) {
            pageIndex--;
        } else {
            pageIndex = 0;
        }
        String whereClause = " where (1=1) ";
        String sqlCount = "select count(entity.id) from Lesson as entity ";
        String sql = "select entity from Lesson as entity ";

//        if (pageRequest.getFromDate() != null) {
//            whereClause += " AND ( entity.createDate >= :fromDate) ";
//        }
//
//        if (pageRequest.getToDate() != null) {
//            whereClause += " AND ( entity.createDate <= :toDate) ";
//        }

        if (pageRequest.getCode() != null) {
            whereClause += " AND ( entity.course.id = :lessonId) ";
        }

        sql += whereClause;
        sqlCount += whereClause;

        TypedQuery<Lesson> q = manager.createQuery(sql, Lesson.class);
        TypedQuery<Long> qCount = manager.createQuery(sqlCount, Long.class);

//		if (dto.getText() != null && StringUtils.hasText(dto.getText())) {
//			q.setParameter("text", '%' + dto.getText().trim() + '%');
//			qCount.setParameter("text", '%' + dto.getText().trim() + '%');
//		}

//        if (pageRequest.getFromDate() != null) {
//            DateTime dateTime = new DateTime(pageRequest.getFromDate());
//            LocalDateTime fromDate = dateTime.toLocalDateTime();
//            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
//            q.setParameter("fromDate", fromDate);
//            qCount.setParameter("fromDate", fromDate);
//        }
//
//        if (pageRequest.getToDate() != null) {
//            DateTime dateTime = new DateTime(pageRequest.getToDate());
//            LocalDateTime toDate = dateTime.toLocalDateTime();
//            q.setParameter("toDate", toDate);
//            qCount.setParameter("toDate", toDate);
//        }
        if (pageRequest.getCode() != null) {
            q.setParameter("lessonId", pageRequest.getCode());
            qCount.setParameter("lessonId", pageRequest.getCode());
        }

        int startPosition = pageIndex * pageSize;
        q.setFirstResult(startPosition);
        q.setMaxResults(pageSize);
        List<Lesson> entities = q.getResultList();
        var lessonDto = entities.stream().map(LessonDto::of).collect(Collectors.toList());
        long count = (long) qCount.getSingleResult();

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<LessonDto> result = new PageImpl<>(lessonDto, pageable, count);

        return BaseResponse.of(result, HttpStatusCodes.OK, HttpStatusMessages.OK);
    }

    private Optional<List<Lesson>> getListLessonByCourseIdFromCache(String courseId) {
        String key = this.keyFrom(courseId);
        log.info("=>>> key cache {}", key);

        String json = this.stringRedisTemplate.opsForValue().get(key);

        return Optional.ofNullable(json)
                .map(jsonValue -> {
                    try {
                        return ObjectMapperUtil.OBJECT_MAPPER.readValue(jsonValue, new TypeReference<>() {});
                    } catch (JsonProcessingException e) {
                        log.error("error cache {}", e.getMessage(), e);

                        return Collections.emptyList();
                    }
                });
    }

    private String keyFrom(String courseId) {
        return "LessonsByCourseId:%s".formatted(courseId);
    }

    private void saveLessonsToCache(String courseId, List<Lesson> lessons) throws JsonProcessingException {
        String key = this.keyFrom(courseId);

        int ttl = ThreadLocalRandom.current().nextInt(cacheTtlMin, cacheTtlMax);

        try {
            this.stringRedisTemplate.opsForValue().set(key, ObjectMapperUtil.OBJECT_MAPPER.writeValueAsString(lessons),
                    ttl, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            log.error("Error serializing lessons to JSON: {}", e.getMessage(), e);
        }
    }
}
