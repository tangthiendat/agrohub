package com.ttdat.authservice.api.controllers;

import com.ttdat.authservice.api.dto.request.CreateUserDTO;
import com.ttdat.authservice.api.dto.response.ApiResponse;
import com.ttdat.authservice.application.commands.CreateUserCommand;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthCommandController {

    private final CommandGateway commandGateway;

    @PostMapping("/new-user")
    public ResponseEntity<ApiResponse<Object>> createUser(@RequestBody CreateUserDTO createUserDTO) {
        CreateUserCommand createUserCommand = CreateUserCommand.builder()
                .userId(UUID.randomUUID())
                .fullName(createUserDTO.getFullName())
                .gender(createUserDTO.getGender())
                .email(createUserDTO.getEmail())
                .password(createUserDTO.getPassword())
                .phoneNumber(createUserDTO.getPhoneNumber())
                .roleId(createUserDTO.getRoleId())
                .build();
        commandGateway.sendAndWait(createUserCommand);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .status(HttpStatus.CREATED.value())
                        .success(true)
                        .message("User created successfully")
                        .build());
    }
}
