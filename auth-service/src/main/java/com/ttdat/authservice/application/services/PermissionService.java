package com.ttdat.authservice.application.services;


import com.ttdat.authservice.api.dto.response.PermissionDTO;
import com.ttdat.authservice.application.exception.ResourceNotFoundException;

public interface PermissionService {
    void createPermission(PermissionDTO permissionDTO);

    void updatePermission(Long id, PermissionDTO permissionDTO) throws ResourceNotFoundException;

    void deletePermission(Long id);
}
