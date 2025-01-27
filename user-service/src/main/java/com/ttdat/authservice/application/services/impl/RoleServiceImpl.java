package com.ttdat.authservice.application.services.impl;

import com.ttdat.authservice.api.dto.common.PermissionDTO;
import com.ttdat.authservice.api.dto.common.RoleDTO;
import com.ttdat.authservice.api.dto.request.UpdateRoleStatusRequest;
import com.ttdat.authservice.application.commands.role.CreateRoleCommand;
import com.ttdat.authservice.application.commands.role.UpdateRoleCommand;
import com.ttdat.authservice.application.commands.role.UpdateRoleStatusCommand;
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
        List<Long> permissionIds = roleDTO.getPermissions() != null ?
                roleDTO.getPermissions().stream()
                        .map(PermissionDTO::getPermissionId)
                        .toList() :
                List.of();
        CreateRoleCommand createRoleCommand = CreateRoleCommand.builder()
                .roleName(roleDTO.getRoleName())
                .description(roleDTO.getDescription())
                .active(roleDTO.isActive())
                .permissionIds(permissionIds)
                .build();
        commandGateway.sendAndWait(createRoleCommand);
    }

    @Override
    public void updateRole(Long id, RoleDTO roleDTO) {
        List<Long> permissionIds = roleDTO.getPermissions() != null ?
                roleDTO.getPermissions().stream()
                        .map(PermissionDTO::getPermissionId)
                        .toList() :
                List.of();
        UpdateRoleCommand updateRoleCommand = UpdateRoleCommand.builder()
                .roleId(id)
                .roleName(roleDTO.getRoleName())
                .description(roleDTO.getDescription())
                .active(roleDTO.isActive())
                .permissionIds(permissionIds)
                .build();
        commandGateway.sendAndWait(updateRoleCommand);
    }

    @Override
    public void updateRoleStatus(Long id, UpdateRoleStatusRequest updateRoleStatusRequest) {
        UpdateRoleStatusCommand updateRoleStatusCommand = UpdateRoleStatusCommand.builder()
                .roleId(id)
                .active(updateRoleStatusRequest.isActive())
                .build();
        commandGateway.sendAndWait(updateRoleStatusCommand);
    }
}
