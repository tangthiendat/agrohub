package com.ttdat.userservice.application.handlers.event;

import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceInUseException;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.userservice.application.constants.RedisKeys;
import com.ttdat.userservice.application.mappers.PermissionMapper;
import com.ttdat.userservice.domain.entities.Permission;
import com.ttdat.userservice.domain.events.permission.PermissionCreatedEvent;
import com.ttdat.userservice.domain.events.permission.PermissionDeletedEvent;
import com.ttdat.userservice.domain.events.permission.PermissionUpdatedEvent;
import com.ttdat.userservice.domain.repositories.PermissionRepository;
import com.ttdat.userservice.infrastructure.services.RedisService;
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
    private final RedisService redisService;

    private void deleteUsersRoleCache() {
        redisService.deleteWithPattern(RedisKeys.USER_PREFIX + ":*:role");
    }

    @Transactional
    @EventHandler
    public void on(PermissionCreatedEvent permissionCreatedEvent) {
        Permission permission = permissionMapper.toEntity(permissionCreatedEvent);
        permissionRepository.save(permission);
        deleteUsersRoleCache();
    }

    private Permission getPermissionById(Long permissionId) {
        return permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PERMISSION_NOT_FOUND));
    }

    @Transactional
    @EventHandler
    public void on(PermissionUpdatedEvent permissionUpdatedEvent) {
        Permission permission = getPermissionById(permissionUpdatedEvent.getPermissionId());
        permissionMapper.updateEntityFromEvent(permission, permissionUpdatedEvent);
        permissionRepository.save(permission);
        deleteUsersRoleCache();
    }

    @Transactional
    @EventHandler
    public void on(PermissionDeletedEvent permissionDeletedEvent) {
        if(permissionRepository.isPermissionInUse(permissionDeletedEvent.getPermissionId())) {
            throw new ResourceInUseException(ErrorCode.PERMISSION_IN_USE);
        }
        Permission permission = getPermissionById(permissionDeletedEvent.getPermissionId());
        permissionRepository.delete(permission);
        deleteUsersRoleCache();
    }
}
