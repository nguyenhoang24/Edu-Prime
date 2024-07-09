package com.eduprime.eduprimeserver.services.impl;

import com.eduprime.eduprimeserver.common.HttpStatusCodes;
import com.eduprime.eduprimeserver.common.HttpStatusMessages;
import com.eduprime.eduprimeserver.domains.Permission;
import com.eduprime.eduprimeserver.domains.Role;
import com.eduprime.eduprimeserver.dtos.BaseResponse;
import com.eduprime.eduprimeserver.dtos.RoleDto;
import com.eduprime.eduprimeserver.dtos.response.MessageResponse;
import com.eduprime.eduprimeserver.repositories.RoleRepository;
import com.eduprime.eduprimeserver.services.PermissionService;
import com.eduprime.eduprimeserver.services.RoleService;
import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final PermissionService permissionService;

    private final EntityManager entityManager;

    @Override
    public Role findByRoleName(String roleName) {
        return this.roleRepository.findByRoleName(roleName).
                orElseThrow(() -> new IllegalArgumentException("Role with roleName = %s does not exist!".formatted(roleName)));
    }

    @Transactional
    @Override
    public BaseResponse<RoleDto> createRole(RoleDto roleDto) {
        Assert.notNull(roleDto.getRoleName(), "roleName not null!");

        String roleName = this.checkRoleName(roleDto);
        Role role = Role.builder()
                .roleName(roleName)
                .description(roleDto.getDescription())
                .build();

        role.setPermissionList(this.createPermissions(roleDto));

        role = this.roleRepository.save(role);

        return BaseResponse.of(RoleDto.of(role), HttpStatusCodes.OK, HttpStatusMessages.OK);
    }

    @Transactional
    @Override
    public BaseResponse<RoleDto> updateRole(RoleDto roleDto, String roleId) {
        Assert.notNull(roleDto.getRoleName(), "roleName not null!");

        Role role = this.getRoleById(roleId);

        role.setRoleName(this.checkRoleName(roleDto));
        role.setDescription(roleDto.getDescription());
        role.setPermissionList(this.createPermissions(roleDto));

        role = this.roleRepository.save(role);

        return BaseResponse.of(RoleDto.of(role), HttpStatusCodes.OK, HttpStatusMessages.OK);
    }

    @Transactional
    @Override
    public MessageResponse deleteRole(String roleId) {
        Role role = this.getRoleById(roleId);

        entityManager.createNativeQuery("DELETE FROM tbl_user_role WHERE role_id = :roleId")
                .setParameter("roleId", roleId)
                .executeUpdate();

        entityManager.createNativeQuery("DELETE FROM tbl_role_permission WHERE role_id = :roleId")
                .setParameter("roleId", roleId)
                .executeUpdate();

        roleRepository.delete(role);

        return MessageResponse.of(HttpStatusCodes.OK, HttpStatusMessages.DELETE_SUCCESS);
    }

    public Role getRoleById(String id) {
        return this.roleRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Role with id = %s not found!".formatted(id)));
    }

    private String checkRoleName(RoleDto roleDto) {
        Role role = this.roleRepository.findByRoleName(roleDto.getRoleName()).orElse(null);
        if (role != null) {
            throw new IllegalArgumentException("Role with roleName = %s already exists.".formatted(roleDto.getRoleName()));
        }
        return roleDto.getRoleName();
    }

    private Set<Permission> createPermissions(RoleDto roleDto) {
        Set<Permission> permissionList = new HashSet<>();
        roleDto.getPermissionList().forEach(permissionDto -> {
            Permission permission = this.permissionService.findByPermissionName(permissionDto);
            permissionList.add(permission);
        });
        return permissionList;
    }
}
