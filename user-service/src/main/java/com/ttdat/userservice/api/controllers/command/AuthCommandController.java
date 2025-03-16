package com.ttdat.userservice.api.controllers.command;

import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.userservice.api.dto.request.AuthRequest;
import com.ttdat.userservice.api.dto.response.AuthResponse;
import com.ttdat.userservice.application.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthCommandController {
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@RequestBody AuthRequest authRequest,
                                           HttpServletResponse response) {
        return ApiResponse.<AuthResponse>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Login successful")
                .payload(authService.login(authRequest, response))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Object> logout(@RequestHeader("Authorization") String authHeader, HttpServletResponse response) {
        authService.logout(authHeader, response);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Logout successful")
                .build();
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthResponse> refreshToken(@CookieValue("refresh_token") String refreshToken) {
        return ApiResponse.<AuthResponse>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Refresh token successful")
                .payload(authService.refreshAccessToken(refreshToken))
                .build();
    }

}
