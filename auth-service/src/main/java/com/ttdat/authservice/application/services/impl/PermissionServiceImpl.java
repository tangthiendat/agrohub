package com.ttdat.authservice.application.services.impl;

import com.ttdat.authservice.api.dto.response.PermissionDTO;
import com.ttdat.authservice.application.commands.permission.CreatePermissionCommand;
import com.ttdat.authservice.application.commands.permission.UpdatePermissionCommand;
import com.ttdat.authservice.application.exception.ResourceNotFoundException;
import com.ttdat.authservice.application.services.PermissionService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final CommandGateway commandGateway;

    @Override
    public void createPermission(PermissionDTO permissionDTO) {
        CreatePermissionCommand createPermissionCommand = CreatePermissionCommand.builder()
                .permissionName(permissionDTO.getPermissionName())
                .description(permissionDTO.getDescription())
                .apiPath(permissionDTO.getApiPath())
                .httpMethod(permissionDTO.getHttpMethod())
                .module(permissionDTO.getModule())
                .build();
        commandGateway.sendAndWait(createPermissionCommand);
    }

    @Override
    public void updatePermission(Long id, PermissionDTO permissionDTO)  throws ResourceNotFoundException {
        UpdatePermissionCommand updatePermissionCommand = UpdatePermissionCommand.builder()
                .permissionId(id)
                .permissionName(permissionDTO.getPermissionName())
                .description(permissionDTO.getDescription())
                .apiPath(permissionDTO.getApiPath())
                .httpMethod(permissionDTO.getHttpMethod())
                .module(permissionDTO.getModule())
                .build();
        commandGateway.sendAndWait(updatePermissionCommand);
    }
}
