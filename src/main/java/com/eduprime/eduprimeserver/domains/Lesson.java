package com.eduprime.eduprimeserver.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tbl_lesson")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lesson extends BaseObject{

    @Column(name = "type")
    private String type;

    @Column(name = "name")
    private String name;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "content")
    private String content;

    @Column(name = "lessonOrder")
    private Integer lessonOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lesson_details_id")
    private LessonDetails lessonDetails;

    @JsonIgnore
    @OneToMany(mappedBy = "lesson", fetch = FetchType.EAGER)
    private List<Item> items;

    @Column(name = "pathFile")
    private String pathFile;

    @Column(name = "image")
    private String image;

    @OneToMany(mappedBy = "lesson", fetch = FetchType.EAGER)
    private List<Comment> comments;

    @OneToMany(mappedBy = "lesson", fetch = FetchType.EAGER)
    private List<Task> tasks;
}
