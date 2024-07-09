package com.eduprime.eduprimeserver.controllers;

import com.eduprime.eduprimeserver.dtos.request.LessonDetailsRequest;
import com.eduprime.eduprimeserver.services.LessonDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lesson-details")
@RequiredArgsConstructor
@Tag(name = "Lesson Details Controller")
public class LessonDetailsController {

    private final LessonDetailsService lessonDetailsService;

    @Operation(summary = "Create lesson details", description = "Them moi chi tiet bai hoc")
    @PostMapping
    public ResponseEntity<?> createLessonDetails(@RequestBody LessonDetailsRequest request) {
        return ResponseEntity.ok(this.lessonDetailsService.createLessonDetails(request));
    }

    @Operation(summary = "Update lesson details", description = "Sua chi tiet bai hoc")
    @PutMapping("/{lessonDetailsId}")
    public ResponseEntity<?> updateLessonDetails(@RequestBody LessonDetailsRequest request, @PathVariable String lessonDetailsId) {
        return ResponseEntity.ok(this.lessonDetailsService.updateLessonDetails(request, lessonDetailsId));
    }
}
