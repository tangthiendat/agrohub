package com.ttdat.authservice.application.services.impl;

import com.ttdat.authservice.api.dto.request.CreateUserRequest;
import com.ttdat.authservice.application.commands.CreateUserCommand;
import com.ttdat.authservice.application.services.UserService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final CommandGateway commandGateway;

    @Override
    public void createUser(CreateUserRequest createUserRequest) {
        CreateUserCommand createUserCommand = CreateUserCommand.builder()
                .userId(UUID.randomUUID())
                .fullName(createUserRequest.getFullName())
                .gender(createUserRequest.getGender())
                .email(createUserRequest.getEmail())
                .password(createUserRequest.getPassword())
                .phoneNumber(createUserRequest.getPhoneNumber())
                .roleId(createUserRequest.getRoleId())
                .build();
        commandGateway.sendAndWait(createUserCommand);
    }
}
