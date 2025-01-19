package com.ttdat.authservice.application.eventhandlers;

import com.ttdat.authservice.application.exception.ErrorCode;
import com.ttdat.authservice.application.exception.ResourceNotFoundException;
import com.ttdat.authservice.application.mappers.PermissionMapper;
import com.ttdat.authservice.domain.entities.Permission;
import com.ttdat.authservice.domain.events.permission.PermissionCreatedEvent;
import com.ttdat.authservice.domain.events.permission.PermissionDeletedEvent;
import com.ttdat.authservice.domain.events.permission.PermissionUpdatedEvent;
import com.ttdat.authservice.domain.repositories.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@ProcessingGroup("permission-group")
public class PermissionEventHandler {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Transactional
    @EventHandler
    public void on(PermissionCreatedEvent permissionCreatedEvent) {
        Permission permission = permissionMapper.toEntity(permissionCreatedEvent);
        permissionRepository.save(permission);
    }

    @Transactional
    @EventHandler
    public void on(PermissionUpdatedEvent permissionUpdatedEvent) {
        Permission permission = permissionRepository.findById(permissionUpdatedEvent.getPermissionId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PERMISSION_NOT_FOUND));
        permissionMapper.updateEntityFromEvent(permission, permissionUpdatedEvent);
        permissionRepository.save(permission);
    }

    @Transactional
    @EventHandler
    public void on(PermissionDeletedEvent permissionDeletedEvent) {
        Permission permission = permissionRepository.findById(permissionDeletedEvent.getPermissionId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PERMISSION_NOT_FOUND));
        permissionRepository.delete(permission);
    }
}
