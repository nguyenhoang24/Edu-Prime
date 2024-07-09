package com.eduprime.eduprimeserver.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_comment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment extends BaseObject{

    @Column(name = "content")
    private String content;

    @Column(name = "timestamp")
    private String timestamp;

    @Column(name = "likeCount")
    private Integer likeCount;

    @Column(name = "editCount")
    private Integer editCount;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "userId")
    private String userId;

//    @Column(name = "parentId")
//    private String parentId;

    @Column(name = "replyCommentId")
    private String replyCommentId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> childComments;
}
