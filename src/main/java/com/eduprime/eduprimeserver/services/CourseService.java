package com.eduprime.eduprimeserver.services;

import com.eduprime.eduprimeserver.domains.Course;
import com.eduprime.eduprimeserver.dtos.BaseResponse;
import com.eduprime.eduprimeserver.dtos.request.CourseRequest;
import com.eduprime.eduprimeserver.dtos.request.PageReq;
import org.springframework.data.domain.Page;

public interface CourseService {

    BaseResponse<?> getAllCourse();

    BaseResponse<?> createCourse(CourseRequest request);

    BaseResponse<?> updateCourse(CourseRequest request, String courseId);

    BaseResponse<?> getCourseById(String courseId);

    Course findCourseById(String courseId);

    BaseResponse<Page> paging(PageReq pageRequest);
}
