package com.ttdat.userservice.application.services.impl;

import com.ttdat.core.api.dto.response.AuthRole;
import com.ttdat.userservice.api.dto.common.UserDTO;
import com.ttdat.userservice.api.dto.request.AuthRequest;
import com.ttdat.userservice.api.dto.response.AuthResponse;
import com.ttdat.userservice.application.mappers.RoleMapper;
import com.ttdat.userservice.application.mappers.UserMapper;
import com.ttdat.userservice.application.queries.user.GetUserByEmailQuery;
import com.ttdat.userservice.application.queries.user.GetUserByIdQuery;
import com.ttdat.userservice.application.services.AuthService;
import com.ttdat.userservice.domain.entities.User;
import com.ttdat.userservice.domain.services.TokenBlacklistService;
import com.ttdat.userservice.infrastructure.services.RedisKeyService;
import com.ttdat.userservice.infrastructure.services.RedisService;
import com.ttdat.userservice.infrastructure.utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final QueryGateway queryGateway;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;
    private final TokenBlacklistService tokenBlacklistService;
    private final RedisService redisService;
    private final RedisKeyService redisKeyService;
    private final RoleMapper roleMapper;

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

        String userRoleKey = redisKeyService.getUserRoleKey(user.getUserId());
        AuthRole authRole = roleMapper.toAuthRole(user.getRole());

        redisService.set(userRoleKey, authRole, 5, TimeUnit.MINUTES);

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
        String userId = jwtUtils.getUserId(token);
        long timeToExpire = jwtUtils.getTokenExpiration(token).toEpochMilli() - System.currentTimeMillis();
        if (timeToExpire > 0) {
            String tokenKey = "token" + ":" + tokenId;
            tokenBlacklistService.blacklistToken(tokenKey, timeToExpire);
        }
        String userRoleKey = redisKeyService.getUserRoleKey(UUID.fromString(userId));
        redisService.delete(userRoleKey);

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
        String userId = jwtUtils.getUserId(refreshToken);
        GetUserByIdQuery getUserByIdQuery = GetUserByIdQuery.builder()
                .userId(userId)
                .build();
        User user = userMapper.toEntity(
                queryGateway.query(getUserByIdQuery, ResponseTypes.instanceOf(UserDTO.class)).join()
        );
        return AuthResponse.builder()
                .accessToken(jwtUtils.generateRefreshToken(user))
                .build();
    }
}
