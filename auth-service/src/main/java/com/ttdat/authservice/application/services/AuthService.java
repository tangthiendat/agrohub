package com.ttdat.authservice.application.services;


import com.ttdat.authservice.api.dto.request.LoginRequest;
import com.ttdat.authservice.api.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest, HttpServletResponse response);
}
