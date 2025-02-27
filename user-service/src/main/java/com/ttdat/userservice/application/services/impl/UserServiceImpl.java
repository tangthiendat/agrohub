package com.ttdat.userservice.application.services.impl;

import com.ttdat.userservice.api.dto.common.UserDTO;
import com.ttdat.userservice.api.dto.request.UpdateUserStatusRequest;
import com.ttdat.userservice.application.commands.user.CreateUserCommand;
import com.ttdat.userservice.application.commands.user.UpdateUserCommand;
import com.ttdat.userservice.application.commands.user.UpdateUserStatusCommand;
import com.ttdat.userservice.application.services.UserService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
                .active(userDTO.isActive())
                .password(userDTO.getPassword())
                .warehouseId(userDTO.getWarehouseId())
                .dob(userDTO.getDob())
                .phoneNumber(userDTO.getPhoneNumber())
                .roleId(userDTO.getRole().getRoleId())
                .build();
        commandGateway.sendAndWait(createUserCommand);
    }

    @Override
    public void updateUser(UUID id, UserDTO userDTO) {
        UpdateUserCommand updateUserCommand = UpdateUserCommand.builder()
                .userId(id)
                .fullName(userDTO.getFullName())
                .gender(userDTO.getGender())
                .email(userDTO.getEmail())
                .active(userDTO.isActive())
                .warehouseId(userDTO.getWarehouseId())
                .dob(userDTO.getDob())
                .phoneNumber(userDTO.getPhoneNumber())
                .roleId(userDTO.getRole().getRoleId())
                .build();
        commandGateway.sendAndWait(updateUserCommand);
    }

    @Override
    public void updateUserStatus(UUID id, UpdateUserStatusRequest updateUserStatusRequest) {
        UpdateUserStatusCommand updateUserStatusCommand = UpdateUserStatusCommand.builder()
                .userId(id)
                .active(updateUserStatusRequest.isActive())
                .build();
        commandGateway.sendAndWait(updateUserStatusCommand);
    }
}
