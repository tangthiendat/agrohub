package com.ttdat.authservice.application.services;

import com.ttdat.authservice.api.dto.response.UserDTO;

public interface UserService {
    void createUser(UserDTO userDTO);
}
