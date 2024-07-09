package com.eduprime.eduprimeserver.controllers;

import com.eduprime.eduprimeserver.common.HttpStatusCodes;
import com.eduprime.eduprimeserver.dtos.BaseResponse;
import com.eduprime.eduprimeserver.dtos.request.LessonRequest;
import com.eduprime.eduprimeserver.dtos.request.PageReq;
import com.eduprime.eduprimeserver.services.LessonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
@Tag(name = "Lesson Controller")
public class LessonController {

    private final LessonService lessonService;

    @Operation(summary = "course details", description = "Lay ra danh sach bai hoc cua khoa hoc")
    @GetMapping("/courses/{courseId}")
    public ResponseEntity<?> getListLessonsByCourseId(@PathVariable String courseId) throws JsonProcessingException {
        return ResponseEntity.ok(this.lessonService.getListLessonByCourseId(courseId));
    }

    @Operation(summary = "Create Lesson", description = "Thêm mới một bài học")
    @PostMapping("/create")
    public ResponseEntity<BaseResponse<LessonRequest>> createLesson(@Valid @RequestBody LessonRequest request) {
        BaseResponse<LessonRequest> response = this.lessonService.createLesson(request);
        return ResponseEntity.status(HttpStatusCodes.CREATED).body(response);
    }

    @Operation(summary = "update Lesson", description = "Sua bai hoc")
    @PutMapping("/{lessonId}/update")
    public ResponseEntity<?> updateLesson(@RequestBody LessonRequest request, @PathVariable String lessonId) {
        return ResponseEntity.ok(this.lessonService.updateLesson(request, lessonId));
    }

    @Operation(summary = "Paging lesson", description = "Phan trang bai hoc")
    @PostMapping("/paging")
    public ResponseEntity<?> pagingLesson(@RequestBody PageReq pageReq) {
        return ResponseEntity.ok(this.lessonService.paging(pageReq));
    }
}
