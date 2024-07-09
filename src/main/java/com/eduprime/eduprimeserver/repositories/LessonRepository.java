package com.eduprime.eduprimeserver.repositories;

import com.eduprime.eduprimeserver.domains.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, String> {

    @Query("SELECT l FROM Lesson l WHERE l.id = :lessonId")
    Optional<Lesson> getLessonById(String lessonId);

    @Query("SELECT l FROM Lesson l WHERE l.course.id = :courseId")
    Optional<List<Lesson>> getListLessonByCourseId(String courseId);

    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.course.id = :courseId")
    int countByCourseId(@Param("courseId") String courseId);
}
