package com.eduprime.eduprimeserver.services;


import com.eduprime.eduprimeserver.domains.Role;
import com.eduprime.eduprimeserver.dtos.BaseResponse;
import com.eduprime.eduprimeserver.dtos.RoleDto;
import com.eduprime.eduprimeserver.dtos.response.MessageResponse;

public interface RoleService {

    Role findByRoleName(String roleName);

    BaseResponse<RoleDto> createRole(RoleDto roleDto);

    BaseResponse<RoleDto> updateRole(RoleDto roleDto, String roleId);

    MessageResponse deleteRole(String roleId);
}
