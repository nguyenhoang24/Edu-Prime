package com.eduprime.eduprimeserver.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LessonDetailsRequest {

    private String title;

    private Integer chapter;

    private String name;

    private String image;

    private String content;

    private String lessonType;

    private String courseId;
}
