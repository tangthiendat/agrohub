package com.ttdat.authservice.application.services.impl;

import com.ttdat.authservice.api.dto.response.PermissionDTO;
import com.ttdat.authservice.application.commands.CreatePermissionCommand;
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
}
