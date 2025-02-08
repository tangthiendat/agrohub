package com.ttdat.userservice.application.handlers.event;

import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.userservice.application.constants.RedisKeys;
import com.ttdat.userservice.application.mappers.RoleMapper;
import com.ttdat.userservice.domain.entities.Role;
import com.ttdat.userservice.domain.events.role.RoleCreatedEvent;
import com.ttdat.userservice.domain.events.role.RoleStatusUpdatedEvent;
import com.ttdat.userservice.domain.events.role.RoleUpdatedEvent;
import com.ttdat.userservice.domain.repositories.RoleRepository;
import com.ttdat.userservice.infrastructure.services.RedisService;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@ProcessingGroup("role-group")
public class RoleEventHandler {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final RedisService redisService;

    private void deleteUsersRoleCache() {
        redisService.deleteWithPattern(RedisKeys.USER_PREFIX + ":*:role");
    }

    @Transactional
    @EventHandler
    public void on(RoleCreatedEvent roleCreatedEvent) {
        Role role = roleMapper.toEntity(roleCreatedEvent);
        roleRepository.save(role);
    }

    @Transactional
    @EventHandler
    public void on(RoleUpdatedEvent roleUpdatedEvent) {
        Role role = roleRepository.findById(roleUpdatedEvent.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ROLE_NOT_FOUND));
        roleMapper.updateEntityFromEvent(role, roleUpdatedEvent);
        roleRepository.save(role);
        deleteUsersRoleCache();
    }

    @Transactional
    @EventHandler
    public void on(RoleStatusUpdatedEvent roleStatusUpdatedEvent) {
        Role role = roleRepository.findById(roleStatusUpdatedEvent.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ROLE_NOT_FOUND));
        role.setActive(roleStatusUpdatedEvent.isActive());
        roleRepository.save(role);
        deleteUsersRoleCache();
    }
}
