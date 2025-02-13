package com.ttdat.userservice.application.services;


import com.ttdat.userservice.api.dto.common.PermissionDTO;

public interface PermissionService {
    void createPermission(PermissionDTO permissionDTO);

    void updatePermission(Long id, PermissionDTO permissionDTO);

    void deletePermission(Long id);
}
