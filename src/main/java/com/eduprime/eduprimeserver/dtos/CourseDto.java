package com.eduprime.eduprimeserver.dtos;

import com.eduprime.eduprimeserver.domains.Course;
import com.eduprime.eduprimeserver.utils.ObjectMapperUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto extends BaseObjectDto {

    private String name;

    private String title;

    private String description;

    private Double price;

    private String category;

    private String image;

    private Integer likeCount;

    private Integer dislikeCount;

    private String teacherId;

    private List<LessonDto> lessons;

    public static final CourseDto of(final Course entity) {
        return ObjectMapperUtil.OBJECT_MAPPER.convertValue(entity, CourseDto.class);
    }
}
