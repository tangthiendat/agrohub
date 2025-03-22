package com.ttdat.userservice.application.services;

import com.ttdat.userservice.api.dto.common.UserDTO;
import com.ttdat.userservice.api.dto.request.UpdateUserStatusRequest;

import java.util.UUID;

public interface UserService {
    void createUser(UserDTO userDTO);

    void updateUser(String id, UserDTO userDTO);

    void updateUserStatus(String id, UpdateUserStatusRequest updateUserStatusRequest);
}
