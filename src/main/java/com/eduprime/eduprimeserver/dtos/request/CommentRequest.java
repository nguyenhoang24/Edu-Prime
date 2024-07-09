package com.eduprime.eduprimeserver.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {

    private String content;

    private String timestamp;

    private Integer likeCount;

    private Integer editCount;

    private String avatar;

    private String userId;

    private String replyCommentId;

//    private LessonDto lesson;

    private String lessonId;

//    private CommentDto parentComment;
    private String parentId;

//    private List<CommentDto> childComments = new ArrayList<>();
}
