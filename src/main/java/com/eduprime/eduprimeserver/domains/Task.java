package com.eduprime.eduprimeserver.domains;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_task")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task extends BaseObject{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private int type;

    @Column(name = "typeName")
    private String typeName;

    @Column(name = "urlPath")
    private String urlPath; // Đường dẫn tới nhiệm vụ cần làm khi click

    @Column(name = "instruction")
    private String instruction; // Hướng dẫn chi tiết cho nhiệm vụ

//    public static final Tasks of(final TasksDto tasks) {
//        return ObjectMapperUtil.OBJECT_MAPPER.convertValue(tasks, Tasks.class);
//    }
}
