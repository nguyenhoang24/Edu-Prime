package com.eduprime.eduprimeserver.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_user_course_item", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "lesson_id" })
})
public class UserLesson extends BaseObject {

    @NotNull
    @Column(name = "user_id")
    private String userId;

    @NotNull
    @Column(name = "lesson_id")
    private String lessonId;

    @NotNull
    @Column(name = "completed")
    private boolean completed;
}
