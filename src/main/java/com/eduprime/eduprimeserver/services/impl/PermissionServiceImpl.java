package com.eduprime.eduprimeserver.services.impl;

import com.eduprime.eduprimeserver.common.HttpStatusCodes;
import com.eduprime.eduprimeserver.common.HttpStatusMessages;
import com.eduprime.eduprimeserver.domains.Permission;
import com.eduprime.eduprimeserver.dtos.BaseResponse;
import com.eduprime.eduprimeserver.dtos.PermissionDto;
import com.eduprime.eduprimeserver.dtos.response.MessageResponse;
import com.eduprime.eduprimeserver.repositories.PermissionRepository;
import com.eduprime.eduprimeserver.services.PermissionService;
import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    private final EntityManager entityManager;

    @Override
    public Permission findByPermissionName(String permissionName) {
        return this.permissionRepository.findByPermissionName(permissionName)
                .orElseThrow(() -> new IllegalArgumentException("Permission with permissionName = %s does not exist!".formatted(permissionName)));
    }

    @Transactional
    @Override
    public BaseResponse<PermissionDto> createPermission(PermissionDto permissionDto) {
        Assert.notNull(permissionDto.getPermissionName(), "permissionName not null!");

        String permissionName = this.checkPermissionName(permissionDto);

        Permission permission = Permission.builder()
                .permissionName(permissionName)
                .description(permissionDto.getDescription())
                .content(permissionDto.getContent())
                .build();

        permission = this.permissionRepository.save(permission);

        return BaseResponse.of(PermissionDto.of(permission), HttpStatusCodes.OK, HttpStatusMessages.OK);
    }

    @Transactional
    @Override
    public BaseResponse<PermissionDto> updatePermission(PermissionDto permissionDto, String permissionId) {
        Assert.notNull(permissionDto.getPermissionName(), "permissionName not null!");

        Permission permission = this.getPermissionById(permissionId);

        permission.setPermissionName(this.checkPermissionName(permissionDto));
        permission.setDescription(permissionDto.getDescription());
        permission.setContent(permissionDto.getContent());

        permission = this.permissionRepository.save(permission);

        return BaseResponse.of(PermissionDto.of(permission), HttpStatusCodes.OK, HttpStatusMessages.OK);
    }

    @Transactional
    @Override
    public MessageResponse deletePermission(String permissionId) {
        Permission permission = this.getPermissionById(permissionId);

        entityManager.createNativeQuery("DELETE FROM tbl_role_permission WHERE permission_id = :permissionId")
                .setParameter("permissionId", permissionId)
                .executeUpdate();

        this.permissionRepository.delete(permission);

        return MessageResponse.of(HttpStatusCodes.OK, HttpStatusMessages.DELETE_SUCCESS);
    }

    public Permission getPermissionById(String permissionId) {
        return this.permissionRepository.findById(permissionId)
                .orElseThrow(() -> new EntityNotFoundException("Permission with id = %s not found!".formatted(permissionId)));
    }

    private String checkPermissionName(PermissionDto permissionDto) {
        Permission permission = this.permissionRepository.findByPermissionName(permissionDto.getPermissionName()).orElse(null);
        if (permission != null) {
            throw new IllegalArgumentException("Permission with permissionName = %s already exists.".formatted(permissionDto.getPermissionName()));
        }
        return permissionDto.getPermissionName();
    }
}
