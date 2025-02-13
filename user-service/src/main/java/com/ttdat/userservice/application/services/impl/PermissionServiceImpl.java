package com.ttdat.userservice.application.services.impl;

import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.userservice.api.dto.common.PermissionDTO;
import com.ttdat.userservice.application.commands.permission.CreatePermissionCommand;
import com.ttdat.userservice.application.commands.permission.DeletePermissionCommand;
import com.ttdat.userservice.application.commands.permission.UpdatePermissionCommand;
import com.ttdat.userservice.application.services.PermissionService;
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

    @Override
    public void deletePermission(Long id) {
        DeletePermissionCommand deletePermissionCommand = DeletePermissionCommand.builder()
                .permissionId(id)
                .build();
        commandGateway.sendAndWait(deletePermissionCommand);
    }
}
