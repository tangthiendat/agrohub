package com.ttdat.userservice.api.controllers.command;

import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.userservice.api.dto.common.RoleDTO;
import com.ttdat.userservice.api.dto.request.UpdateRoleStatusRequest;
import com.ttdat.userservice.application.services.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleCommandController {
    private final RoleService roleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createRole(@RequestBody @Valid RoleDTO roleDTO) {
        roleService.createRole(roleDTO);
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .message("Role created successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<Object> updateRole(@PathVariable Long id, @RequestBody @Valid RoleDTO roleDTO) {
        roleService.updateRole(id, roleDTO);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Role updated successfully")
                .build();
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Object> updateRoleStatus(@PathVariable Long id, @RequestBody UpdateRoleStatusRequest updateRoleStatusRequest) {
        roleService.updateRoleStatus(id, updateRoleStatusRequest);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Role status updated successfully")
                .build();
    }

}
