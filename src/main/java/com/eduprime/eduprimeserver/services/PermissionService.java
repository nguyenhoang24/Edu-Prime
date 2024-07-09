package com.eduprime.eduprimeserver.services;


import com.eduprime.eduprimeserver.domains.Permission;
import com.eduprime.eduprimeserver.dtos.BaseResponse;
import com.eduprime.eduprimeserver.dtos.PermissionDto;
import com.eduprime.eduprimeserver.dtos.response.MessageResponse;

public interface PermissionService {
    Permission findByPermissionName(String permissionName);

    BaseResponse<PermissionDto> createPermission(PermissionDto permissionDto);

    BaseResponse<PermissionDto> updatePermission(PermissionDto permissionDto, String permissionId);

    MessageResponse deletePermission(String permissionId);
}
