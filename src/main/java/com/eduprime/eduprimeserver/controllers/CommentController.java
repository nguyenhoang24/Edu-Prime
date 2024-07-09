package com.eduprime.eduprimeserver.controllers;

import com.eduprime.eduprimeserver.dtos.request.CommentRequest;
import com.eduprime.eduprimeserver.dtos.request.PageReq;
import com.eduprime.eduprimeserver.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Tag(name = "Comment Controller")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Create Comment", description = "Tao moi binh luan")
    @PostMapping("/lessons/{lessonId}")
    public ResponseEntity<?> createComment(@RequestBody CommentRequest request, @PathVariable String lessonId) {
        return ResponseEntity.ok(this.commentService.createComment(request, lessonId));
    }

    @Operation(summary = "Update Comment", description = "Sua binh luan")
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@RequestBody CommentRequest request, @PathVariable String commentId) {
        return ResponseEntity.ok(this.commentService.updateComment(request, commentId));
    }

    @Operation(summary = "Get list comment by lessonId", description = "Lay danh sach binh luan theo bai hoc")
    @GetMapping("/lessons/{lessonId}")
    public ResponseEntity<?> getListCommentByLessonId(@PathVariable String lessonId) {
        return ResponseEntity.ok(this.commentService.getListCommentByLessonId(lessonId));
    }

    @Operation(summary = "Delete comment", description = "Xoa binh luan")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable String commentId) {
        return ResponseEntity.ok(this.commentService.deleteComment(commentId));
    }

    @Operation(summary = "Paging comment", description = "Phan trang binh luan")
    @PostMapping("/paging")
    public ResponseEntity<?> pagingComment(@RequestBody PageReq pageReq) {
        return ResponseEntity.ok(this.commentService.paging(pageReq));
    }
}
