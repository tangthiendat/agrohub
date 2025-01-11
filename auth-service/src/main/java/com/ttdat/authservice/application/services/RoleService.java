package com.ttdat.authservice.application.services;

import com.ttdat.authservice.api.dto.common.RoleDTO;

public interface RoleService {
    void createRole(RoleDTO roleDTO);

    void updateRole(Long id, RoleDTO roleDTO);
}
