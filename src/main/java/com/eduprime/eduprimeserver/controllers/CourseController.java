package com.eduprime.eduprimeserver.controllers;

import com.eduprime.eduprimeserver.common.SecurityConstants;
import com.eduprime.eduprimeserver.dtos.request.CourseRequest;
import com.eduprime.eduprimeserver.dtos.request.PageReq;
import com.eduprime.eduprimeserver.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@Tag(name = "Course Controller")
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "Get all course", description = "Lay danh sach tat ca khoa hoc")
    @GetMapping
    public ResponseEntity<?> getAllCourse() {
        return ResponseEntity.ok(this.courseService.getAllCourse());
    }

    @Operation(summary = "Create Course", description = "Them khoa hoc")
    @PostMapping
//    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> createCourse(@RequestBody CourseRequest request) {
        return ResponseEntity.ok(this.courseService.createCourse(request));
    }

    @Operation(summary = "Update Course", description = "Sua khoa hoc")
    @PutMapping("/{courseId}")
//    @PreAuthorize("hasAuthority('" + SecurityConstants.ROLE_ADMIN + "')")
    public ResponseEntity<?> updateCourse(@RequestBody CourseRequest request, @PathVariable String courseId) {
        return ResponseEntity.ok(this.courseService.updateCourse(request, courseId));
    }

    @Operation(summary = "Get course by id", description = "Lay khoa hoc theo id")
    @GetMapping("/{courseId}")
    public ResponseEntity<?> getCourseById(@PathVariable String courseId) {
        return ResponseEntity.ok(this.courseService.getCourseById(courseId));
    }

    @Operation(summary = "Paging course", description = "Phan trang khoa hoc")
    @PostMapping("/paging")
    public ResponseEntity<?> pagingLesson(@RequestBody PageReq pageReq) {
        return ResponseEntity.ok(this.courseService.paging(pageReq));
    }
}
