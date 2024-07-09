package com.eduprime.eduprimeserver.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LessonRequest {

    private String name; // bắt buộc

    private String title; // bắt buộc

    private String description;

    private String content;

    private String courseId; // bắt buộc

//    private String lessonDetailsId; // bắt buộc

//    private String pathFile;

    private String image;
}
