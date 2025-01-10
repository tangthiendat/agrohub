package com.ttdat.authservice.application.services;

import com.ttdat.authservice.api.dto.UserDTO;

public interface UserService {
    void createUser(UserDTO userDTO);
}
