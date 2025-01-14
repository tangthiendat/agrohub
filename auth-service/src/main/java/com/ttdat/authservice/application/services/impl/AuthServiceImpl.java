package com.ttdat.authservice.application.services.impl;

import com.ttdat.authservice.api.dto.common.UserDTO;
import com.ttdat.authservice.api.dto.request.AuthRequest;
import com.ttdat.authservice.api.dto.response.AuthResponse;
import com.ttdat.authservice.application.mappers.UserMapper;
import com.ttdat.authservice.application.queries.user.GetUserByEmailQuery;
import com.ttdat.authservice.application.services.AuthService;
import com.ttdat.authservice.domain.entities.User;
import com.ttdat.authservice.domain.services.TokenBlacklistService;
import com.ttdat.authservice.infrastructure.utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;
    private final TokenBlacklistService tokenBlacklistService;
    @Override
    public AuthResponse login(AuthRequest authRequest, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        GetUserByEmailQuery getUserByEmailQuery = GetUserByEmailQuery.builder()
                .email(authRequest.getEmail())
                .build();
        User user = userMapper.toEntity(
                queryGateway.query(getUserByEmailQuery, ResponseTypes.instanceOf(UserDTO.class)).join()
        );
        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        // Store refresh token in http only cookie
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setAttribute("SameSite", "Strict");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days

        response.addCookie(refreshTokenCookie);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    @Override
    public void logout(String authHeader, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        String token = authHeader.substring(7);
        String tokenId = jwtUtils.getTokenId(token);
        long timeToExpire = jwtUtils.getTokenExpiration(token).toEpochMilli() - System.currentTimeMillis();
        if (timeToExpire > 0) {
            String tokenKey = "token" + ":" + tokenId;
            tokenBlacklistService.blacklistToken(tokenKey, timeToExpire);
        }

        // Remove refresh token from http only cookie
        Cookie refreshTokenCookie = new Cookie("refresh_token", "");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setAttribute("SameSite", "Strict");
        refreshTokenCookie.setMaxAge(0);

        response.addCookie(refreshTokenCookie);
    }

    @Override
    public AuthResponse refreshAccessToken(String refreshToken) {
        String email = jwtUtils.getUsername(refreshToken);
        GetUserByEmailQuery getUserByEmailQuery = GetUserByEmailQuery.builder()
                .email(email)
                .build();
        User user = userMapper.toEntity(
                queryGateway.query(getUserByEmailQuery, ResponseTypes.instanceOf(UserDTO.class)).join()
        );
        return AuthResponse.builder()
                .accessToken(jwtUtils.generateRefreshToken(user))
                .build();
    }
}
