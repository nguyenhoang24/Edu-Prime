package com.eduprime.eduprimeserver.domains;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_user_tasks", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "task_id" })
})
public class UserTask extends BaseObject {

    @NotNull
    @Column(name = "user_id")
    private String userId;

    @NotNull
    @Column(name = "task_id")
    private String tasksId;

    @NotNull
    @Column(name = "completed")
    private boolean completed;

    @NotNull
    @Column(name = "completion_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completionTime; // Thời gian hoàn thành nhiệm vụ (nếu có)
}
