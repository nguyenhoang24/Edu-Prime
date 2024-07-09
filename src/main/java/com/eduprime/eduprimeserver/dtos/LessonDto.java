package com.eduprime.eduprimeserver.dtos;

import com.eduprime.eduprimeserver.domains.Lesson;
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
public class LessonDto extends BaseObjectDto {

    private String type;

    private String name;

    private String title;

    private String description;

    private String content;

    private Integer lessonOrder;

    private CourseDto course;

    private LessonDetailsDto lessonDetails;

    private List<ItemDto> items;

    private String pathFile;

    private String image;

    private List<CommentDto> comments;

    private List<TaskDto> tasks;

    public static final LessonDto of(final Lesson entity) {
        return ObjectMapperUtil.OBJECT_MAPPER.convertValue(entity, LessonDto.class);
    }
}
