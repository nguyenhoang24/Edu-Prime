package com.eduprime.eduprimeserver.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_user_course", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "course_id" })
})
public class UserCourse extends BaseObject {
    @NotNull
    @Column(name = "registration_time")
    private Date registrationTime;

    @NotNull
    @Column(name = "completion_time")
    private Date completionTime;

    @NotNull
    @Column(name = "user_id")
    private String userId;

    @NotNull
    @Column(name = "course_id")
    private String courseId;

}
