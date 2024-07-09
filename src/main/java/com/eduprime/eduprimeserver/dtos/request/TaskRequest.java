package com.eduprime.eduprimeserver.dtos.request;

import com.eduprime.eduprimeserver.dtos.LessonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {

    private String lessonId;

    private String title;

    private String description;

    private int type;

    private String typeName;

    private String urlPath; // Đường dẫn tới nhiệm vụ cần làm khi click

    private String instruction; // Hướng dẫn chi tiết cho nhiệm vụ
}
