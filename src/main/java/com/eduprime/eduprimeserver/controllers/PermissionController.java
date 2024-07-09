package com.eduprime.eduprimeserver.controllers;

import com.eduprime.eduprimeserver.dtos.BaseResponse;
import com.eduprime.eduprimeserver.dtos.PermissionDto;
import com.eduprime.eduprimeserver.services.PermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@Tag(name = "Permission Controller")
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<BaseResponse> createPermission(@RequestBody PermissionDto permissionDto) {
        return ResponseEntity.ok(this.permissionService.createPermission(permissionDto));
    }

    @PutMapping("/{permissionId}")
    public ResponseEntity<BaseResponse> updatePermission(@RequestBody PermissionDto permissionDto, @PathVariable String permissionId) {
        return ResponseEntity.ok(this.permissionService.updatePermission(permissionDto, permissionId));
    }

    @DeleteMapping("{permissionId}")
    public ResponseEntity<?> deletePermission(@PathVariable String permissionId) {
        return ResponseEntity.ok(this.permissionService.deletePermission(permissionId));
    }
}
