package com.eduprime.eduprimeserver.services;

import com.eduprime.eduprimeserver.domains.Comment;
import com.eduprime.eduprimeserver.dtos.BaseResponse;
import com.eduprime.eduprimeserver.dtos.request.CommentRequest;
import com.eduprime.eduprimeserver.dtos.request.PageReq;
import org.springframework.data.domain.Page;

public interface CommentService {

    BaseResponse<?> createComment(CommentRequest request, String lessonId);

    BaseResponse<?> updateComment(CommentRequest request, String commentId);

    BaseResponse<?> deleteComment(String commentId);

    BaseResponse<?> getListCommentByLessonId(String lessonId);

    Comment findCommentById(String commentId);

    BaseResponse<Page> paging(PageReq pageRequest);
}
