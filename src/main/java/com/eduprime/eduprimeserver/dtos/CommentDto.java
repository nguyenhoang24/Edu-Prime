package com.eduprime.eduprimeserver.dtos;

import com.eduprime.eduprimeserver.domains.Comment;
import com.eduprime.eduprimeserver.utils.ObjectMapperUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto extends BaseObjectDto {

    private String content;

    private String timestamp;

    private Integer likeCount;

    private Integer editCount;

    private String avatar;

    private String userId;

//    private String parentId;

    private String replyCommentId;

    private LessonDto lesson;

    private CommentDto parentComment;

    private List<CommentDto> childComments = new ArrayList<>();

    public static final CommentDto of(final Comment comment) {
        return ObjectMapperUtil.OBJECT_MAPPER.convertValue(comment, CommentDto.class);
    }
}
