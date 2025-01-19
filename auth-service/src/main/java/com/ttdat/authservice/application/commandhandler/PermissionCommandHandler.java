package com.ttdat.authservice.application.commandhandler;

import com.ttdat.authservice.application.commands.permission.CreatePermissionCommand;
import com.ttdat.authservice.application.commands.permission.DeletePermissionCommand;
import com.ttdat.authservice.application.commands.permission.UpdatePermissionCommand;
import com.ttdat.authservice.domain.events.permission.PermissionCreatedEvent;
import com.ttdat.authservice.domain.events.permission.PermissionDeletedEvent;
import com.ttdat.authservice.domain.events.permission.PermissionUpdatedEvent;
import com.ttdat.authservice.infrastructure.services.IdGeneratorService;
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
