package com.ttdat.authservice.application.services;


import com.ttdat.authservice.api.dto.common.PermissionDTO;

public interface PermissionService {
    void createPermission(PermissionDTO permissionDTO);

    void updatePermission(Long id, PermissionDTO permissionDTO);

    void deletePermission(Long id);
}
