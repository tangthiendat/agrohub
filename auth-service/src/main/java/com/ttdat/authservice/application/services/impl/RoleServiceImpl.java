package com.ttdat.authservice.application.services.impl;

import com.ttdat.authservice.api.dto.PermissionDTO;
import com.ttdat.authservice.api.dto.RoleDTO;
import com.ttdat.authservice.application.commands.role.CreateRoleCommand;
import com.ttdat.authservice.application.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final CommandGateway commandGateway;

    @Override
    public void createRole(RoleDTO roleDTO) {
        List<Long> permissionIds = roleDTO.getPermissions().stream()
                .map(PermissionDTO::getPermissionId)
                .toList();
        CreateRoleCommand createRoleCommand = CreateRoleCommand.builder()
                .roleName(roleDTO.getRoleName())
                .description(roleDTO.getDescription())
                .active(roleDTO.isActive())
                .permissionIds(permissionIds)
                .build();
        commandGateway.sendAndWait(createRoleCommand);
    }
}
