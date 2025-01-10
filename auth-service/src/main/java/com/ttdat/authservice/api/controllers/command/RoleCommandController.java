package com.ttdat.authservice.api.controllers.command;

import com.ttdat.authservice.api.dto.RoleDTO;
import com.ttdat.authservice.api.dto.response.ApiResponse;
import com.ttdat.authservice.application.services.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
