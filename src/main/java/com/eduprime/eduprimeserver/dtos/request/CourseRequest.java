package com.eduprime.eduprimeserver.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequest {

    private String name;

    private String title;

    private String description;

    private Double price;

    private String category;

    private String image;

//    private Integer likeCount;
//
//    private Integer dislikeCount;

//    private String teacherId;
}
