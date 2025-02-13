package com.ttdat.userservice.application.handlers.command;

import com.ttdat.userservice.application.commands.user.CreateUserCommand;
import com.ttdat.userservice.application.commands.user.UpdateUserCommand;
import com.ttdat.userservice.application.commands.user.UpdateUserStatusCommand;
import com.ttdat.userservice.domain.events.user.UserCreatedEvent;
import com.ttdat.userservice.domain.events.user.UserStatusUpdatedEvent;
import com.ttdat.userservice.domain.events.user.UserUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserCommandHandler {
    private final EventBus eventBus;

    @CommandHandler
    public void handle(CreateUserCommand createUserCommand) {
        UserCreatedEvent userCreatedEvent = UserCreatedEvent.builder()
                .userId(UUID.randomUUID())
                .fullName(createUserCommand.getFullName())
                .gender(createUserCommand.getGender())
                .email(createUserCommand.getEmail())
                .active(createUserCommand.isActive())
                .dob(createUserCommand.getDob())
                .password(createUserCommand.getPassword())
                .phoneNumber(createUserCommand.getPhoneNumber())
                .roleId(createUserCommand.getRoleId())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(userCreatedEvent));
    }

    @CommandHandler
    public void handle(UpdateUserCommand updateUserCommand) {
        UserUpdatedEvent userUpdatedEvent = UserUpdatedEvent.builder()
                .userId(updateUserCommand.getUserId())
                .fullName(updateUserCommand.getFullName())
                .gender(updateUserCommand.getGender())
                .email(updateUserCommand.getEmail())
                .active(updateUserCommand.isActive())
                .dob(updateUserCommand.getDob())
                .phoneNumber(updateUserCommand.getPhoneNumber())
                .roleId(updateUserCommand.getRoleId())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(userUpdatedEvent));
    }

    @CommandHandler
    public void handle(UpdateUserStatusCommand updateUserStatusCommand) {
        UserStatusUpdatedEvent userStatusUpdatedEvent = UserStatusUpdatedEvent.builder()
                .userId(updateUserStatusCommand.getUserId())
                .active(updateUserStatusCommand.isActive())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(userStatusUpdatedEvent));
    }
}
