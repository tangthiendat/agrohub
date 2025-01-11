package com.ttdat.authservice.api.controllers.command;

import com.ttdat.authservice.api.dto.common.PermissionDTO;
import com.ttdat.authservice.api.dto.response.ApiResponse;
import com.ttdat.authservice.application.services.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
public class PermissionCommandController {
    private final PermissionService permissionService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createPermission(@Valid @RequestBody PermissionDTO permissionDTO) {
        permissionService.createPermission(permissionDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .status(HttpStatus.CREATED.value())
                        .success(true)
                        .message("Permission created successfully")
                        .build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> updatePermission(@Valid @PathVariable Long id, @RequestBody PermissionDTO permissionDTO) {
        permissionService.updatePermission(id, permissionDTO);
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Permission updated successfully")
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Permission deleted successfully")
                .build());
    }
}
