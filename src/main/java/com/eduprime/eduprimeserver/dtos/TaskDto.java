package com.eduprime.eduprimeserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto extends BaseObjectDto {

    private LessonDto lesson;

    private String title;

    private String description;

    private int type;

    private String typeName;

    private String urlPath; // Đường dẫn tới nhiệm vụ cần làm khi click

    private String instruction; // Hướng dẫn chi tiết cho nhiệm vụ
}
