package com.ttdat.authservice.application.services;

import com.ttdat.authservice.api.dto.request.CreateUserRequest;

public interface UserService {
    void createUser(CreateUserRequest createUserRequest);
}
