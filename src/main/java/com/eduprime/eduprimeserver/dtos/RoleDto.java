package com.eduprime.eduprimeserver.dtos;

import com.eduprime.eduprimeserver.domains.Permission;
import com.eduprime.eduprimeserver.domains.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@JsonIgnoreProperties
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto extends BaseObjectDto{

    private String roleName;

    private String description;

    private Set<String> permissionList;

    public static final RoleDto of(final Role role) {
        RoleDto dto = new RoleDto();
        dto.setRoleName(role.getRoleName());
        dto.setDescription(role.getDescription());
        dto.setPermissionList(role.getPermissionList().stream()
                .map(Permission::getPermissionName)
                .collect(Collectors.toSet()));

        return dto;
    }
}
