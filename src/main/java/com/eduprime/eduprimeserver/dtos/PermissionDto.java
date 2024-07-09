package com.eduprime.eduprimeserver.dtos;

import com.eduprime.eduprimeserver.domains.Permission;
import com.eduprime.eduprimeserver.utils.ObjectMapperUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDto extends BaseObjectDto{

    private String permissionName;

    private String description;

    private String content;

    public static PermissionDto of(final Permission permission) {
        return ObjectMapperUtil.OBJECT_MAPPER.convertValue(permission, PermissionDto.class);
    }
}
