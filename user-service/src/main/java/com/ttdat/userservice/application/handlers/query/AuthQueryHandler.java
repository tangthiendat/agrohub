package com.ttdat.userservice.application.handlers.query;

import com.ttdat.core.api.dto.response.AuthRole;
import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.core.application.queries.auth.CheckPermissionQuery;
import com.ttdat.core.application.queries.auth.IsTokenBlacklistedQuery;
import com.ttdat.userservice.application.constants.RedisKeys;
import com.ttdat.userservice.application.mappers.RoleMapper;
import com.ttdat.userservice.domain.entities.User;
import com.ttdat.userservice.domain.repositories.UserRepository;
import com.ttdat.userservice.domain.services.TokenBlacklistService;
import com.ttdat.userservice.infrastructure.services.RedisService;
import com.ttdat.userservice.infrastructure.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthQueryHandler {
    private final UserRepository userRepository;
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtUtils jwtUtils;
    private final RoleMapper roleMapper;
    private final RedisService redisService;

    @QueryHandler
    public boolean handle(CheckPermissionQuery checkPermissionQuery) {
        AuthRole authRole = getAuthRole(checkPermissionQuery.getUserId());
        return authRole.isActive() && authRole.getPermissions().stream()
                .anyMatch(permission -> permission.getApiPath().equals(checkPermissionQuery.getApiPath())
                        && permission.getHttpMethod().equals(checkPermissionQuery.getHttpMethod()));
    }

    private AuthRole getAuthRole(String userId) {
        String userRoleKey = RedisKeys.USER_PREFIX + ":" + userId + ":role";
        AuthRole authRole = (AuthRole) redisService.get(userRoleKey);
        if (authRole == null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
            authRole = roleMapper.toAuthRole(user.getRole());
            redisService.set(userRoleKey, authRole, 5, TimeUnit.MINUTES);
        }
        return authRole;
    }


    @QueryHandler
    public boolean handle(IsTokenBlacklistedQuery isTokenBlacklistedQuery) {
        String tokenId = jwtUtils.getTokenId(isTokenBlacklistedQuery.getToken());
        return tokenBlacklistService.isBlacklisted(tokenId);
    }


}
