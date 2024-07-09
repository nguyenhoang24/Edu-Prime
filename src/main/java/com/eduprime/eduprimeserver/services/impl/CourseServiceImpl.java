package com.eduprime.eduprimeserver.services.impl;

import com.eduprime.eduprimeserver.common.HttpStatusCodes;
import com.eduprime.eduprimeserver.common.HttpStatusMessages;
import com.eduprime.eduprimeserver.domains.Course;
import com.eduprime.eduprimeserver.dtos.BaseResponse;
import com.eduprime.eduprimeserver.dtos.CourseDto;
import com.eduprime.eduprimeserver.dtos.request.CourseRequest;
import com.eduprime.eduprimeserver.dtos.request.PageReq;
import com.eduprime.eduprimeserver.exceptions.BadRequestException;
import com.eduprime.eduprimeserver.exceptions.ForbiddenException;
import com.eduprime.eduprimeserver.exceptions.UnauthorizedException;
import com.eduprime.eduprimeserver.repositories.CourseRepository;
import com.eduprime.eduprimeserver.services.CourseService;
import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    private final EntityManager manager;

    @Value("${baseUrl.local.view-image}")
    private String baseUrl;

    @Override
    public BaseResponse<?> getAllCourse() {
        var courses = this.courseRepository.getAllCourse();

        if (courses.isPresent()) {
            var coursesResponse = courses.get().stream()
                    .map(CourseDto::of)
                    .collect(Collectors.toList());

            return BaseResponse.of(coursesResponse, HttpStatusCodes.OK, HttpStatusMessages.OK);
        }

        return BaseResponse.of(null, HttpStatusCodes.NOT_FOUND, HttpStatusMessages.NOT_FOUND);
    }

    @Transactional
    @Override
    public BaseResponse<?> createCourse(CourseRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new UnauthorizedException("Bạn cần đăng nhập để thực hiện chức năng này!");
        }

        boolean hasRoleAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!hasRoleAdmin) {
            throw new ForbiddenException("Bạn không có quyền truy cập chức năng này!");
        }
        Assert.notNull(request.getName(), "Không để trống tên khóa học!");
        Assert.notNull(request.getTitle(), "Không để trống tiêu đề khóa học!");

        var course = Course.builder()
                .name(request.getName())
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .image(baseUrl + request.getImage())
                .likeCount(0)
                .dislikeCount(0)
//                .teacherId(request.getTeacherId())
                .build();

        course = this.courseRepository.save(course);

        return BaseResponse.of(CourseDto.of(course), HttpStatusCodes.OK, HttpStatusMessages.OK);
    }

    @Transactional
    @Override
    public BaseResponse<?> updateCourse(CourseRequest request, String courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new UnauthorizedException("Bạn cần đăng nhập để thực hiện chức năng này!");
        }

        boolean hasRoleAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!hasRoleAdmin) {
            throw new ForbiddenException("Bạn không có quyền truy cập chức năng này!");
        }
        Assert.notNull(courseId, "courseId dùng để cập nhật không được bỏ trống!");
        Assert.notNull(request.getName(), "Không để trống tên khóa học!");
        Assert.notNull(request.getTitle(), "Không để trống tiêu đề khóa học!");

        var course = this.courseRepository.getCourseById(courseId);

        if (course.isPresent()) {
            var courseResponse = course.get();

            courseResponse.setName(request.getName());
            courseResponse.setTitle(request.getTitle());
            courseResponse.setDescription(request.getDescription());
            courseResponse.setPrice(request.getPrice());
            courseResponse.setCategory(request.getCategory());
            courseResponse.setImage(baseUrl + request.getImage());
//            courseResponse.setTeacherId(request.getTeacherId());

            courseResponse = this.courseRepository.save(courseResponse);

            return BaseResponse.of(CourseDto.of(courseResponse), HttpStatusCodes.OK, HttpStatusMessages.OK);
        }

        log.error("course with id = %s not found".formatted(courseId));

        return BaseResponse.error(HttpStatusCodes.NOT_FOUND, HttpStatusMessages.NOT_FOUND);
    }

    @Override
    public BaseResponse<?> getCourseById(String courseId) {
        Assert.notNull(courseId, "courseId not null!");

        return BaseResponse.of(CourseDto.of(this.findCourseById(courseId)),
                HttpStatusCodes.OK, HttpStatusMessages.OK);
    }

    @Override
    public Course findCourseById(String courseId) {
        return this.courseRepository.getCourseById(courseId).get();
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
        String sqlCount = "select count(entity.id) from Course as entity ";
        String sql = "select entity from Course as entity ";

        if (pageRequest.getFromDate() != null) {
            whereClause += " AND ( entity.createDate >= :fromDate) ";
        }

        if (pageRequest.getToDate() != null) {
            whereClause += " AND ( entity.createDate <= :toDate) ";
        }

//        if (pageRequest.getCode() != null) {
//            whereClause += " AND ( entity.course.id = :lessonId) ";
//        }
        if (pageRequest.getName() != null) {
            whereClause += " AND ( entity.name LIKE :name  " +
                    "OR entity.title LIKE :title) ";
        }

        sql += whereClause;
        sqlCount += whereClause;

        TypedQuery<Course> q = manager.createQuery(sql, Course.class);
        TypedQuery<Long> qCount = manager.createQuery(sqlCount, Long.class);

        if (pageRequest.getName() != null) {
            String namePattern = "%" + pageRequest.getName() + "%";

            q.setParameter("name", namePattern);
            qCount.setParameter("name", namePattern);

            q.setParameter("title", namePattern);
            qCount.setParameter("title", namePattern);
        }

        if (pageRequest.getFromDate() != null) {
            DateTime dateTime = new DateTime(pageRequest.getFromDate());
            LocalDateTime fromDate = dateTime.toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            q.setParameter("fromDate", fromDate);
            qCount.setParameter("fromDate", fromDate);
        }

        if (pageRequest.getToDate() != null) {
            DateTime dateTime = new DateTime(pageRequest.getToDate());
            LocalDateTime toDate = dateTime.toLocalDateTime();
            q.setParameter("toDate", toDate);
            qCount.setParameter("toDate", toDate);
        }

        int startPosition = pageIndex * pageSize;
        q.setFirstResult(startPosition);
        q.setMaxResults(pageSize);
        List<Course> entities = q.getResultList();
        var courseDto = entities.stream().map(CourseDto::of).collect(Collectors.toList());
        long count = (long) qCount.getSingleResult();

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<CourseDto> result = new PageImpl<>(courseDto, pageable, count);

        return BaseResponse.of(result, HttpStatusCodes.OK, HttpStatusMessages.OK);
    }
}
