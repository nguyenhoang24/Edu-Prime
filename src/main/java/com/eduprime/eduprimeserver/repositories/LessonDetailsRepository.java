package com.eduprime.eduprimeserver.repositories;

import com.eduprime.eduprimeserver.domains.LessonDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface LessonDetailsRepository extends JpaRepository<LessonDetails, String> {

    @Query("SELECT ld FROM LessonDetails ld WHERE ld.id = :lessonDetailsId")
    Optional<LessonDetails> getLessonDetailsById(@Param("lessonDetailsId") String lessonDetailsId);
}
