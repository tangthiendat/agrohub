package com.ttdat.userservice.application.services;

import com.ttdat.userservice.api.dto.common.RoleDTO;
import com.ttdat.userservice.api.dto.request.UpdateRoleStatusRequest;
import jakarta.validation.Valid;

public interface RoleService {
    void createRole(RoleDTO roleDTO);

    void updateRole(Long id, RoleDTO roleDTO);

    void updateRoleStatus(@Valid Long id, UpdateRoleStatusRequest updateRoleStatusRequest);
}
