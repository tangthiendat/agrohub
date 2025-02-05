package com.ttdat.userservice.application.queryhandlers;

import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.core.application.queries.auth.CheckPermissionQuery;
import com.ttdat.core.application.queries.auth.IsTokenBlacklistedQuery;
import com.ttdat.userservice.domain.entities.User;
import com.ttdat.userservice.domain.repositories.UserRepository;
import com.ttdat.userservice.domain.services.TokenBlacklistService;
import com.ttdat.userservice.infrastructure.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@RequiredArgsConstructor
public class AuthQueryHandler {
    private final UserRepository userRepository;
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtUtils jwtUtils;

    @QueryHandler
    public boolean handle(CheckPermissionQuery checkPermissionQuery){
        User user = userRepository.findById(UUID.fromString(checkPermissionQuery.getUserId()))
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
        return user.getRole().isActive() && user.getRole().getPermissions().stream()
                .anyMatch(permission -> permission.getApiPath().equals(checkPermissionQuery.getPath())
                        && permission.getHttpMethod().equals(checkPermissionQuery.getHttpMethod()));
    }

    @QueryHandler
    public boolean handle(IsTokenBlacklistedQuery isTokenBlacklistedQuery){
        String tokenId = jwtUtils.getTokenId(isTokenBlacklistedQuery.getToken());
        return tokenBlacklistService.isBlacklisted(tokenId);
    }


}
