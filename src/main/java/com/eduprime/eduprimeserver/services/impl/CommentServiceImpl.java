package com.eduprime.eduprimeserver.services.impl;

import com.eduprime.eduprimeserver.common.HttpStatusCodes;
import com.eduprime.eduprimeserver.common.HttpStatusMessages;
import com.eduprime.eduprimeserver.domains.Comment;
import com.eduprime.eduprimeserver.dtos.BaseResponse;
import com.eduprime.eduprimeserver.dtos.CommentDto;
import com.eduprime.eduprimeserver.dtos.request.CommentRequest;
import com.eduprime.eduprimeserver.dtos.request.PageReq;
import com.eduprime.eduprimeserver.repositories.CommentRepository;
import com.eduprime.eduprimeserver.services.CommentService;
import com.eduprime.eduprimeserver.services.LessonService;
import com.eduprime.eduprimeserver.services.UserService;
import com.eduprime.eduprimeserver.utils.DateTimeUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final LessonService lessonService;

    private final UserService userService;

    private final EntityManager manager;

    @Override
    public BaseResponse<?> createComment(CommentRequest request, String lessonId) {
        Assert.notNull(request.getContent(), "content comment not null!");
        Assert.notNull(request.getUserId(), "userId not null!");
        Assert.notNull(lessonId, "lessonId not null!");

        // validate user đã mua khóa học chưa, lesson
        var lesson = this.lessonService.findLessonById(lessonId);

        Comment parentComment = null;
        if (request.getParentId() != null) {
            parentComment = this.findCommentById(request.getParentId());
            if (parentComment.getParentComment() != null) {
                throw new IllegalArgumentException("Cannot add replies to a comment that already has replies.");
            }
        } else {
            if (request.getReplyCommentId() != null) {
                throw new IllegalArgumentException("Cannot add replies to a comment because don't have parent comment.");
            }
        }

        var entity = Comment.builder()
                .content(request.getContent())
                .timestamp(DateTimeUtils.timeNow())
                .likeCount(0)
                .editCount(0)
                .avatar(this.userService.findUserById(request.getUserId()).getProfile())
                .replyCommentId(request.getReplyCommentId())
                .userId(request.getUserId())
//                .parentComment(this.findCommentById(request.getParentId()))
                .lesson(lesson)
                .build();

        entity = this.commentRepository.save(entity);

        if (parentComment != null) {
            parentComment.getChildComments().add(entity);
            this.commentRepository.save(parentComment);
        }

        return BaseResponse.of(CommentDto.of(entity), HttpStatusCodes.OK, HttpStatusMessages.OK);
    }

    @Override
    public BaseResponse<?> updateComment(CommentRequest request, String commentId) {
        Assert.notNull(commentId, "Id comment cannot be null!");
        Assert.notNull(request.getContent(), "Content cannot be null!");
        Assert.notNull(request.getUserId(), "userId cannot be null!");
        Assert.notNull(request.getLessonId(), "lessonId cannot be null!");

        //validate user
//        this.validateUser(dto.getUserId(), dto.getCourseItemId());

        var entity = this.findCommentById(commentId);

        List<Comment> childComments = Optional.ofNullable(entity.getParentComment().getId())
                .map(parentId -> this.commentRepository.getListCommentByParentCommentId(parentId))
                .orElse(null);

        entity.setContent(request.getContent());
        entity.setEditCount(entity.getEditCount() + 1);

        entity = this.commentRepository.save(entity);

        Comment finalEntity = entity;
        Optional.ofNullable(childComments)
                .ifPresent(comments -> {
                    comments.replaceAll(childComment -> {
                        if (childComment.getId().equals(finalEntity.getId())) {
                            childComment.setContent(finalEntity.getContent());
                            childComment.setEditCount(finalEntity.getEditCount());
                        }
                        return childComment;
                    });

                    Comment parentComment = this.findCommentById(finalEntity.getParentComment().getId());
                    parentComment.setChildComments(comments);
                    commentRepository.save(parentComment);
                });

        return BaseResponse.of(CommentDto.of(entity), HttpStatusCodes.OK, HttpStatusMessages.OK);
    }

    @Override
    public BaseResponse<?> deleteComment(String commentId) {
        Assert.notNull(commentId, "Id comment cannot be null!");

        var comment = this.findCommentById(commentId);

        Optional.ofNullable(this.commentRepository.getListCommentByParentCommentId(commentId))
                .ifPresent(childComments -> childComments.forEach(this.commentRepository::delete));

        this.commentRepository.delete(comment);

        return BaseResponse.message(HttpStatusCodes.OK, HttpStatusMessages.OK + " Delete Successfully!");
    }

    @Override
    public BaseResponse<?> getListCommentByLessonId(String lessonId) {
        Assert.notNull(lessonId, "lessonId not null!");

        var comments = this.commentRepository.getListCommentByLessonId(lessonId);

        var commentsDto = comments.get().stream()
                .map(CommentDto::of)
                .collect(Collectors.toList());

        return BaseResponse.of(commentsDto, HttpStatusCodes.OK, HttpStatusMessages.OK);
    }

    @Override
    public Comment findCommentById(String commentId) {
        return this.commentRepository.getCommentById(commentId).get();
    }

    @Override
    public BaseResponse<Page> paging(PageReq pageRequest) {
        if (pageRequest.getPageIndex() == null || pageRequest.getPageSize() == null) {
            return BaseResponse.error(HttpStatusCodes.BAD_REQUEST, HttpStatusMessages.BAD_REQUEST
                    + " Invalid page request");
        }

        int pageIndex = pageRequest.getPageIndex();
        int pageSize = pageRequest.getPageSize();

        if (pageIndex > 0) {
            pageIndex--;
        } else {
            pageIndex = 0;
        }
        String whereClause = " where (1=1) ";
        String sqlCount = "select count(entity.id) from Comment as entity ";
        String sql = "select entity from Comment as entity ";

//        if (pageRequest.getFromDate() != null) {
//            whereClause += " AND ( entity.createDate >= :fromDate) ";
//        }
//
//        if (pageRequest.getToDate() != null) {
//            whereClause += " AND ( entity.createDate <= :toDate) ";
//        }
        sql += whereClause;
        sqlCount += whereClause;

        TypedQuery<Comment> q = manager.createQuery(sql, Comment.class);
        TypedQuery<Long> qCount = manager.createQuery(sqlCount, Long.class);

//		if (dto.getText() != null && StringUtils.hasText(dto.getText())) {
//			q.setParameter("text", '%' + dto.getText().trim() + '%');
//			qCount.setParameter("text", '%' + dto.getText().trim() + '%');
//		}

        if (pageRequest.getFromDate() != null) {
            DateTime dateTime = new DateTime(pageRequest.getFromDate());
            LocalDateTime fromDate = dateTime.toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            q.setParameter("fromDate", fromDate);
            qCount.setParameter("fromDate", fromDate);
        }

        if (pageRequest.getToDate() != null) {
            DateTime dateTime = new DateTime(pageRequest.getToDate());
            LocalDateTime toDate = dateTime.toLocalDateTime();
            q.setParameter("toDate", toDate);
            qCount.setParameter("toDate", toDate);
        }

        int startPosition = pageIndex * pageSize;
        q.setFirstResult(startPosition);
        q.setMaxResults(pageSize);
        List<Comment> entities = q.getResultList();
        var commentDto = entities.stream().map(CommentDto::of).collect(Collectors.toList());
        long count = (long) qCount.getSingleResult();

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<CommentDto> result = new PageImpl<>(commentDto, pageable, count);

        return BaseResponse.of(result, HttpStatusCodes.OK, HttpStatusMessages.OK);
    }
}
