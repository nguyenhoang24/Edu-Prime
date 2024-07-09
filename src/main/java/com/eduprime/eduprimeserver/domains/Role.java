package com.eduprime.eduprimeserver.domains;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "tbl_role")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role extends BaseObject {

    @Column(name = "roleName", nullable = false, unique = true)
    private String roleName;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "roleList", fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    private Set<User> userList;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tbl_role_permission", joinColumns = @JoinColumn(name = "roleId"), inverseJoinColumns = @JoinColumn(name = "permissionId"))
    @JsonManagedReference
    private Set<Permission> permissionList;

}
