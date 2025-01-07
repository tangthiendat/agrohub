package com.ttdat.authservice.application.eventhandlers;

import com.ttdat.authservice.application.mappers.PermissionMapper;
import com.ttdat.authservice.domain.entities.Permission;
import com.ttdat.authservice.domain.events.PermissionCreatedEvent;
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
}
