package com.ttdat.authservice.api.controllers.command;

import com.ttdat.authservice.api.dto.common.RoleDTO;
import com.ttdat.authservice.api.dto.request.UpdateRoleStatusRequest;
import com.ttdat.authservice.api.dto.response.ApiResponse;
import com.ttdat.authservice.application.services.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleCommandController {
    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createRole(@Valid @RequestBody RoleDTO roleDTO) {
        roleService.createRole(roleDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .status(HttpStatus.CREATED.value())
                        .success(true)
                        .message("Role created successfully")
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> updateRole(@Valid @PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        roleService.updateRole(id, roleDTO);
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Role updated successfully")
                .build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Object>> updateRoleStatus(@Valid @PathVariable Long id, @RequestBody UpdateRoleStatusRequest updateRoleStatusRequest) {
        roleService.updateRoleStatus(id, updateRoleStatusRequest);
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Role status updated successfully")
                .build());
    }

}
