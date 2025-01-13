package com.ttdat.authservice.application.services;


import com.ttdat.authservice.api.dto.request.AuthRequest;
import com.ttdat.authservice.api.dto.response.AuthResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthResponse login(AuthRequest loginRequest, HttpServletResponse response);

    void logout(String authHeader, HttpServletResponse response);

    AuthResponse refreshAccessToken(String refreshToken);
}
