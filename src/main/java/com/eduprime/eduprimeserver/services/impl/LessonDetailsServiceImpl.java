package com.eduprime.eduprimeserver.services.impl;

import com.eduprime.eduprimeserver.common.HttpStatusCodes;
import com.eduprime.eduprimeserver.common.HttpStatusMessages;
import com.eduprime.eduprimeserver.domains.LessonDetails;
import com.eduprime.eduprimeserver.dtos.BaseResponse;
import com.eduprime.eduprimeserver.dtos.LessonDetailsDto;
import com.eduprime.eduprimeserver.dtos.request.LessonDetailsRequest;
import com.eduprime.eduprimeserver.repositories.LessonDetailsRepository;
import com.eduprime.eduprimeserver.services.CourseService;
import com.eduprime.eduprimeserver.services.LessonDetailsService;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LessonDetailsServiceImpl implements LessonDetailsService {

    private final LessonDetailsRepository lessonDetailsRepository;

    private final CourseService courseService;

    @Transactional
    @Override
    public BaseResponse<?> createLessonDetails(LessonDetailsRequest request) {
        Assert.notNull(request.getName(), "name lessonDetails not null!");
        Assert.notNull(request.getCourseId(), "courseId not null!");

        var lessonDetails = LessonDetails.builder()
                .title(request.getTitle())
                .chapter(request.getChapter())
                .name(request.getName())
                .image(request.getImage())
                .content(request.getContent())
                .lessonType(request.getLessonType())
                .course(this.courseService.findCourseById(request.getCourseId()))
                .build();

        lessonDetails = this.lessonDetailsRepository.save(lessonDetails);

        return BaseResponse.of(LessonDetailsDto.of(lessonDetails),
                HttpStatusCodes.OK, HttpStatusMessages.OK);
    }

    @Transactional
    @Override
    public BaseResponse<?> updateLessonDetails(LessonDetailsRequest request, String lessonDetailsId) {
        Assert.notNull(lessonDetailsId, "lessonDetailsId not null!");
        Assert.notNull(request.getName(), "name lessonDetails not null!");

        var lessonDetails = this.lessonDetailsRepository.getLessonDetailsById(lessonDetailsId).get();

        if (lessonDetails == null) {
            return BaseResponse.error(HttpStatusCodes.NOT_FOUND, HttpStatusMessages.NOT_FOUND +
                    " LessonDetails with lessonDetailsId = %s does exist!".formatted(lessonDetailsId));
        }

        lessonDetails.setTitle(request.getTitle());
        lessonDetails.setChapter(request.getChapter());
        lessonDetails.setName(request.getName());
        lessonDetails.setImage(request.getImage());
        lessonDetails.setContent(request.getContent());
        lessonDetails.setLessonType(request.getLessonType());
        lessonDetails.setCourse(this.courseService.findCourseById(request.getCourseId()));

        lessonDetails = this.lessonDetailsRepository.save(lessonDetails);

        return BaseResponse.of(LessonDetailsDto.of(lessonDetails), HttpStatusCodes.OK, HttpStatusMessages.OK);
    }

    @Override
    public BaseResponse<?> deleteLessonDetails(String lessonDetailsId) {
        return null;
    }
}
