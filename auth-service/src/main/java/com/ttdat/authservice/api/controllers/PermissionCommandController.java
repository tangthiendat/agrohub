package com.ttdat.authservice.api.controllers;

import com.ttdat.authservice.api.dto.response.ApiResponse;
import com.ttdat.authservice.api.dto.response.PermissionDTO;
import com.ttdat.authservice.application.services.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
public class PermissionCommandController {
    private final PermissionService permissionService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createPermission(@RequestBody PermissionDTO permissionDTO) {
        permissionService.createPermission(permissionDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .status(HttpStatus.CREATED.value())
                        .success(true)
                        .message("Permission created successfully")
                        .build());
    }
}
