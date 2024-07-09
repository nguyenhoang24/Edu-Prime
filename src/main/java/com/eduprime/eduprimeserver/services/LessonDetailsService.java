package com.eduprime.eduprimeserver.services;

import com.eduprime.eduprimeserver.dtos.BaseResponse;
import com.eduprime.eduprimeserver.dtos.request.LessonDetailsRequest;

public interface LessonDetailsService {

    BaseResponse<?> createLessonDetails(LessonDetailsRequest request);

    BaseResponse<?> updateLessonDetails(LessonDetailsRequest request, String lessonDetailsId);

    BaseResponse<?> deleteLessonDetails(String lessonDetailsId);
}
