package com.ttdat.authservice.application.services.impl;

import com.ttdat.authservice.api.dto.response.UserDTO;
import com.ttdat.authservice.application.commands.user.CreateUserCommand;
import com.ttdat.authservice.application.services.UserService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final CommandGateway commandGateway;

    @Override
    public void createUser(UserDTO userDTO) {
        CreateUserCommand createUserCommand = CreateUserCommand.builder()
                .fullName(userDTO.getFullName())
                .gender(userDTO.getGender())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .phoneNumber(userDTO.getPhoneNumber())
                .roleId(userDTO.getRole().getRoleId())
                .build();
        commandGateway.sendAndWait(createUserCommand);
    }
}
