package com.eduprime.eduprimeserver.domains;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_lesson_details")
public class LessonDetails extends BaseObject{

    @Column(name = "title")
    private String title;

    @Column(name = "chapter")
    private Integer chapter;

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "content")
    private String content;

    @Column(name = "lessonType")
    private String lessonType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Course course;
}
