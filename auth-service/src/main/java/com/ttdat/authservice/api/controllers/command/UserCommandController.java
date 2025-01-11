package com.ttdat.authservice.api.controllers.command;

import com.ttdat.authservice.api.dto.common.UserDTO;
import com.ttdat.authservice.api.dto.response.ApiResponse;
import com.ttdat.authservice.application.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserCommandController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createUser(@RequestBody UserDTO userDTO) {
        userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .status(HttpStatus.CREATED.value())
                        .success(true)
                        .message("User created successfully")
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> updateUser(@PathVariable UUID id, @RequestBody UserDTO userDTO) {
        userService.updateUser(id, userDTO);
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("User updated successfully")
                .build());
    }

}
