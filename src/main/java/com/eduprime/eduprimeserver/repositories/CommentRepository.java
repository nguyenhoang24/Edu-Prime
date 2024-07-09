package com.eduprime.eduprimeserver.repositories;

import com.eduprime.eduprimeserver.domains.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

    @Query("SELECT c FROM Comment c WHERE c.id = :commentId")
    Optional<Comment> getCommentById(@Param("commentId") String commentId);

    @Query("SELECT c FROM Comment c WHERE c.parentComment.id = :parentCommentId")
    List<Comment> getListCommentByParentCommentId(@Param("parentCommentId") String parentCommentId);

    @Query("SELECT c FROM Comment c WHERE c.lesson.id = :lessonId")
    Optional<List<Comment>> getListCommentByLessonId(@Param("lessonId") String lessonId);
}
