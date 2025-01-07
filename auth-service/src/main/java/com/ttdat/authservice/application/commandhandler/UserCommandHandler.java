package com.ttdat.authservice.application.commandhandler;

import com.ttdat.authservice.application.commands.CreateUserCommand;
import com.ttdat.authservice.domain.events.UserCreatedEvent;
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
    public void handle(CreateUserCommand createUserCommand){
        UserCreatedEvent userCreatedEvent = UserCreatedEvent.builder()
                .userId(UUID.randomUUID())
                .fullName(createUserCommand.getFullName())
                .gender(createUserCommand.getGender())
                .email(createUserCommand.getEmail())
                .password(createUserCommand.getPassword())
                .phoneNumber(createUserCommand.getPhoneNumber())
                .roleId(createUserCommand.getRoleId())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(userCreatedEvent));
    }
}
