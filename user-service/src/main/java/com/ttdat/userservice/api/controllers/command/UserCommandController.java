package com.ttdat.userservice.api.controllers.command;

import com.ttdat.userservice.api.dto.common.UserDTO;
import com.ttdat.userservice.api.dto.request.UpdateUserStatusRequest;
import com.ttdat.userservice.api.dto.response.ApiResponse;
import com.ttdat.userservice.application.services.UserService;
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

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Object>> updateUserStatus(@PathVariable UUID id, @RequestBody UpdateUserStatusRequest updateUserStatusRequest) {
        userService.updateUserStatus(id, updateUserStatusRequest);
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("User status updated successfully")
                .build());
    }

}
