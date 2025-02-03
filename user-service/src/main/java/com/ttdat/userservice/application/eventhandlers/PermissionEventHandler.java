package com.ttdat.userservice.application.eventhandlers;

import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceInUseException;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.userservice.application.mappers.PermissionMapper;
import com.ttdat.userservice.domain.entities.Permission;
import com.ttdat.userservice.domain.events.permission.PermissionCreatedEvent;
import com.ttdat.userservice.domain.events.permission.PermissionDeletedEvent;
import com.ttdat.userservice.domain.events.permission.PermissionUpdatedEvent;
import com.ttdat.userservice.domain.repositories.PermissionRepository;
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
        if(permissionRepository.isPermissionInUse(permissionDeletedEvent.getPermissionId())) {
            throw new ResourceInUseException(ErrorCode.PERMISSION_IN_USE);
        }
        Permission permission = permissionRepository.findById(permissionDeletedEvent.getPermissionId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PERMISSION_NOT_FOUND));
        permissionRepository.delete(permission);
    }
}
