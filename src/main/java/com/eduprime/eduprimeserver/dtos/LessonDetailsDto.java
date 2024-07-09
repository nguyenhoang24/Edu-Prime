package com.eduprime.eduprimeserver.dtos;

import com.eduprime.eduprimeserver.domains.LessonDetails;
import com.eduprime.eduprimeserver.utils.ObjectMapperUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LessonDetailsDto extends BaseObjectDto {

    private String title;

    private Integer chapter;

    private String name;

    private String image;

    private String content;

    private String lessonType;

    private CourseDto course;

    public static final LessonDetailsDto of(final LessonDetails entity) {
        return ObjectMapperUtil.OBJECT_MAPPER.convertValue(entity, LessonDetailsDto.class);
    }
}
