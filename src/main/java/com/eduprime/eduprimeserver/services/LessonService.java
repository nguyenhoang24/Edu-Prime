package com.eduprime.eduprimeserver.services;

import com.eduprime.eduprimeserver.domains.Lesson;
import com.eduprime.eduprimeserver.dtos.BaseResponse;
import com.eduprime.eduprimeserver.dtos.request.LessonRequest;
import com.eduprime.eduprimeserver.dtos.request.PageReq;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;

public interface LessonService {

    BaseResponse<?> getListLessonByCourseId(String courseId) throws JsonProcessingException;

    BaseResponse<?> getLessonById(String lessonId);

    BaseResponse<LessonRequest> createLesson(LessonRequest request);

    BaseResponse<?> updateLesson(LessonRequest request, String lessonId);

    Lesson findLessonById(String lessonId);

    BaseResponse<?> deleteLesson(String lessonId);

    BaseResponse<Page> paging(PageReq pageRequest);

}
