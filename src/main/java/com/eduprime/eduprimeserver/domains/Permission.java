package com.eduprime.eduprimeserver.domains;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "tbl_permission")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Permission extends BaseObject {

    @Column(name = "permissionName", nullable = false, unique = true)
    private String permissionName;

    @Column(name = "description")
    private String description;

    @Column(name = "content")
    private String content;

    @ManyToMany(mappedBy = "permissionList", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Role> roleList;
}
