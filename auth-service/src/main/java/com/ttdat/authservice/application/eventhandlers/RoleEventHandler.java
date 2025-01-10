package com.ttdat.authservice.application.eventhandlers;

import com.ttdat.authservice.application.exception.ErrorCode;
import com.ttdat.authservice.application.exception.ResourceNotFoundException;
import com.ttdat.authservice.application.mappers.RoleMapper;
import com.ttdat.authservice.domain.entities.Role;
import com.ttdat.authservice.domain.events.role.RoleCreatedEvent;
import com.ttdat.authservice.domain.events.role.RoleUpdatedEvent;
import com.ttdat.authservice.domain.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("role-group")
public class RoleEventHandler {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @EventHandler
    public void on(RoleCreatedEvent roleCreatedEvent) {
        Role role = roleMapper.toRole(roleCreatedEvent);
        roleRepository.save(role);
    }

    @EventHandler
    public void on(RoleUpdatedEvent roleUpdatedEvent) {
        Role role = roleRepository.findById(roleUpdatedEvent.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ROLE_NOT_FOUND));
        roleMapper.updateRoleFromEvent(role, roleUpdatedEvent);
        roleRepository.save(role);
    }
}
