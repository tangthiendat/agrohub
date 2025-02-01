package com.ttdat.userservice.api.controllers.command;

import com.ttdat.userservice.api.dto.request.AuthRequest;
import com.ttdat.userservice.api.dto.response.ApiResponse;
import com.ttdat.userservice.api.dto.response.AuthResponse;
import com.ttdat.userservice.application.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthCommandController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest authRequest,
                                                           HttpServletResponse response) {
        return ResponseEntity.ok(ApiResponse.<AuthResponse>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Login successful")
                .payload(authService.login(authRequest, response))
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout(@RequestHeader("Authorization") String authHeader, HttpServletResponse response) {
        authService.logout(authHeader, response);
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Logout successful")
                .build());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@CookieValue("refresh_token") String refreshToken) {
        return ResponseEntity.ok(ApiResponse.<AuthResponse>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Refresh token successful")
                .payload(authService.refreshAccessToken(refreshToken))
                .build());
    }

}
