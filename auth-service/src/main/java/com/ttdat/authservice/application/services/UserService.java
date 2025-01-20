package com.ttdat.authservice.application.services;

import com.ttdat.authservice.api.dto.common.UserDTO;
import com.ttdat.authservice.api.dto.request.UpdateUserStatusRequest;

import java.util.UUID;

public interface UserService {
    void createUser(UserDTO userDTO);

    void updateUser(UUID id, UserDTO userDTO);

    void updateUserStatus(UUID id, UpdateUserStatusRequest updateUserStatusRequest);
}
