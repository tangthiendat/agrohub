package com.ttdat.userservice.application.handlers.command;

import com.ttdat.userservice.application.commands.permission.CreatePermissionCommand;
import com.ttdat.userservice.application.commands.permission.DeletePermissionCommand;
import com.ttdat.userservice.application.commands.permission.UpdatePermissionCommand;
import com.ttdat.userservice.domain.events.permission.PermissionCreatedEvent;
import com.ttdat.userservice.domain.events.permission.PermissionDeletedEvent;
import com.ttdat.userservice.domain.events.permission.PermissionUpdatedEvent;
import com.ttdat.userservice.infrastructure.services.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionCommandHandler {
    private final EventBus eventBus;
    private final IdGeneratorService idGeneratorService;

    @CommandHandler
    public void handle(CreatePermissionCommand createPermissionCommand){
        PermissionCreatedEvent permissionCreatedEvent = PermissionCreatedEvent.builder()
                .permissionId(idGeneratorService.generatePermissionId())
                .permissionName(createPermissionCommand.getPermissionName())
                .description(createPermissionCommand.getDescription())
                .apiPath(createPermissionCommand.getApiPath())
                .httpMethod(createPermissionCommand.getHttpMethod())
                .module(createPermissionCommand.getModule())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(permissionCreatedEvent));
    }

    @CommandHandler
    public void handle(UpdatePermissionCommand updatePermissionCommand)  {
        PermissionUpdatedEvent permissionUpdatedEvent = PermissionUpdatedEvent.builder()
                .permissionId(updatePermissionCommand.getPermissionId())
                .permissionName(updatePermissionCommand.getPermissionName())
                .description(updatePermissionCommand.getDescription())
                .apiPath(updatePermissionCommand.getApiPath())
                .httpMethod(updatePermissionCommand.getHttpMethod())
                .module(updatePermissionCommand.getModule())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(permissionUpdatedEvent));
    }

    @CommandHandler
    public void handle(DeletePermissionCommand deletePermissionCommand) {
        PermissionDeletedEvent permissionDeletedEvent = PermissionDeletedEvent.builder()
                .permissionId(deletePermissionCommand.getPermissionId())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(permissionDeletedEvent));
    }
}
