package com.eduprime.eduprimeserver.services;

import com.eduprime.eduprimeserver.dtos.BaseResponse;
import com.eduprime.eduprimeserver.dtos.request.TaskRequest;

public interface TaskService {

    BaseResponse<?> createTask(TaskRequest request);

    BaseResponse<?> updateTask(TaskRequest request, String taskId);

    BaseResponse<?> deleteTask(String taskId);

    BaseResponse<?> getListTaskByLessonId(String lessonId);

    BaseResponse<?> getListTaskByCourseId(String courseId);
}
