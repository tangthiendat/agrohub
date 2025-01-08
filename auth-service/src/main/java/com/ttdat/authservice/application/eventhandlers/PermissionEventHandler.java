package com.ttdat.authservice.application.eventhandlers;

import com.ttdat.authservice.application.exception.ErrorCode;
import com.ttdat.authservice.application.exception.ResourceNotFoundException;
import com.ttdat.authservice.application.mappers.PermissionMapper;
import com.ttdat.authservice.domain.entities.Permission;
import com.ttdat.authservice.domain.events.PermissionCreatedEvent;
import com.ttdat.authservice.domain.events.PermissionUpdatedEvent;
import com.ttdat.authservice.domain.repositories.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("permission-group")
public class PermissionEventHandler {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @EventHandler
    public void on(PermissionCreatedEvent permissionCreatedEvent) {
        Permission permission = permissionMapper.toPermission(permissionCreatedEvent);
        permissionRepository.save(permission);
    }

    @EventHandler
    public void on(PermissionUpdatedEvent permissionUpdatedEvent) {
        Permission permission = permissionRepository.findById(permissionUpdatedEvent.getPermissionId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PERMISSION_NOT_FOUND));
        permissionMapper.updatePermissionFromEvent(permission, permissionUpdatedEvent);
        permissionRepository.save(permission);
    }
}
