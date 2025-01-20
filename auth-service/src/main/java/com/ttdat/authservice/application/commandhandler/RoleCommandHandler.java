package com.ttdat.authservice.application.commandhandler;

import com.ttdat.authservice.application.commands.role.CreateRoleCommand;
import com.ttdat.authservice.application.commands.role.UpdateRoleCommand;
import com.ttdat.authservice.application.commands.role.UpdateRoleStatusCommand;
import com.ttdat.authservice.domain.events.role.RoleCreatedEvent;
import com.ttdat.authservice.domain.events.role.RoleStatusUpdatedEvent;
import com.ttdat.authservice.domain.events.role.RoleUpdatedEvent;
import com.ttdat.authservice.infrastructure.services.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleCommandHandler {
    private final EventBus eventBus;
    private final IdGeneratorService idGeneratorService;

    @CommandHandler
    public void handle(CreateRoleCommand createRoleCommand) {
        RoleCreatedEvent roleCreatedEvent = RoleCreatedEvent.builder()
                .roleId(idGeneratorService.generateRoleId())
                .roleName(createRoleCommand.getRoleName())
                .description(createRoleCommand.getDescription())
                .active(createRoleCommand.isActive())
                .permissionIds(createRoleCommand.getPermissionIds())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(roleCreatedEvent));
    }

    @CommandHandler
    public void handle(UpdateRoleCommand updateRoleCommand) {
        RoleUpdatedEvent roleUpdatedEvent = RoleUpdatedEvent.builder()
                .roleId(updateRoleCommand.getRoleId())
                .roleName(updateRoleCommand.getRoleName())
                .description(updateRoleCommand.getDescription())
                .active(updateRoleCommand.isActive())
                .permissionIds(updateRoleCommand.getPermissionIds())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(roleUpdatedEvent));
    }

    @CommandHandler
    public void handle(UpdateRoleStatusCommand updateRoleStatusCommand) {
        RoleStatusUpdatedEvent roleStatusUpdatedEvent = RoleStatusUpdatedEvent.builder()
                .roleId(updateRoleStatusCommand.getRoleId())
                .active(updateRoleStatusCommand.isActive())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(roleStatusUpdatedEvent));
    }

}
