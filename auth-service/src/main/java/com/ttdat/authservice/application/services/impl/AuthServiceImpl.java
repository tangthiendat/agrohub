package com.ttdat.authservice.application.services.impl;

import com.ttdat.authservice.api.dto.request.LoginRequest;
import com.ttdat.authservice.api.dto.response.LoginResponse;
import com.ttdat.authservice.api.dto.response.UserDTO;
import com.ttdat.authservice.application.mappers.UserMapper;
import com.ttdat.authservice.application.queries.FindUserByEmailQuery;
import com.ttdat.authservice.application.services.AuthService;
import com.ttdat.authservice.domain.entities.User;
import com.ttdat.authservice.infrastructure.utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;

    @Override
    public LoginResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        FindUserByEmailQuery findUserByEmailQuery = FindUserByEmailQuery.builder()
                .email(loginRequest.getEmail())
                .build();
        User user = userMapper.toUser(
                queryGateway.query(findUserByEmailQuery, ResponseTypes.instanceOf(UserDTO.class)).join()
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

        return LoginResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
