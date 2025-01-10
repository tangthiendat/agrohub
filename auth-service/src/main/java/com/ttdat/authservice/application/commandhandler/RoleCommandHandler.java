package com.ttdat.authservice.application.commandhandler;

import com.ttdat.authservice.application.commands.role.CreateRoleCommand;
import com.ttdat.authservice.domain.events.role.RoleCreatedEvent;
import com.ttdat.authservice.domain.services.IdGeneratorService;
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

}
