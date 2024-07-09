package com.eduprime.eduprimeserver.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tbl_course")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course extends BaseObject {

    @Column(name = "name")
    private String name;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "category")
    private String category;

    @Column(name = "image")
    private String image;

    @Column(name = "likeCount")
    private Integer likeCount;

    @Column(name = "dislikeCount")
    private Integer dislikeCount;

//    @OneToOne(mappedBy = "course")
    @Column(name = "teacherId")
    private String teacherId;

    @JsonIgnore
    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    private Set<Lesson> lessons;

    @JsonIgnore
    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    private Set<LessonDetails> lessonDetails;
}
