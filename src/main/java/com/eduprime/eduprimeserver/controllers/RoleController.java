package com.eduprime.eduprimeserver.controllers;

import com.eduprime.eduprimeserver.dtos.BaseResponse;
import com.eduprime.eduprimeserver.dtos.RoleDto;
import com.eduprime.eduprimeserver.services.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Tag(name = "Role Controller")
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<BaseResponse> createRole(@RequestBody RoleDto roleDto) {
        return ResponseEntity.ok(this.roleService.createRole(roleDto));
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<BaseResponse> updateRole(@RequestBody RoleDto roleDto, @PathVariable String roleId) {
        return ResponseEntity.ok(this.roleService.updateRole(roleDto, roleId));
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable String roleId) {
        return ResponseEntity.ok(this.roleService.deleteRole(roleId));
    }
}
