package com.ttdat.authservice.application.services;


import com.ttdat.authservice.api.dto.response.PermissionDTO;

public interface PermissionService {
    void createPermission(PermissionDTO permissionDTO);
}
