package com.eduprime.eduprimeserver.repositories;

import com.eduprime.eduprimeserver.domains.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    @Query("SELECT c FROM Course c")
    Optional<List<Course>> getAllCourse();

    @Query("SELECT c FROM Course c WHERE c.id = :courseId")
    Optional<Course> getCourseById(String courseId);
}
