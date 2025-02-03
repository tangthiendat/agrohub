package com.ttdat.userservice.application.queryhandlers;

import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.core.application.queries.auth.CheckPermissionQuery;
import com.ttdat.core.application.queries.auth.GetAuthenticationByIdQuery;
import com.ttdat.core.application.queries.auth.IsTokenBlacklistedQuery;
import com.ttdat.userservice.application.mappers.UserMapper;
import com.ttdat.userservice.domain.entities.Role;
import com.ttdat.userservice.domain.entities.User;
import com.ttdat.userservice.domain.repositories.UserRepository;
import com.ttdat.userservice.domain.services.TokenBlacklistService;
import com.ttdat.userservice.infrastructure.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@RequiredArgsConstructor
public class AuthQueryHandler {
    private final UserRepository userRepository;
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;

    @QueryHandler
    public boolean handle(CheckPermissionQuery checkPermissionQuery){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Role authRole = (Role) authentication.getAuthorities().stream().findFirst().orElse(null);
        if(authRole == null){
            return false;
        }
        return authRole.isActive() && authRole.getPermissions().stream()
                .anyMatch(permission -> permission.getApiPath().equals(checkPermissionQuery.getPath())
                        && permission.getHttpMethod().equals(checkPermissionQuery.getHttpMethod()));
    }

    @QueryHandler
    public boolean handle(IsTokenBlacklistedQuery isTokenBlacklistedQuery){
        String tokenId = jwtUtils.getTokenId(isTokenBlacklistedQuery.getToken());
        return tokenBlacklistService.isBlacklisted(tokenId);
    }

    @QueryHandler
    public Authentication handle(GetAuthenticationByIdQuery getAuthenticationByIdQuery){
        User user = userRepository.findById(UUID.fromString(getAuthenticationByIdQuery.getUserId()))
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
        return new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
    }

//    @QueryHandler
//    public AuthUser handle(GetAuthenticationByIdQuery getAuthenticationByIdQuery){
//        User user = userRepository.findById(UUID.fromString(getAuthenticationByIdQuery.getUserId()))
//                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
//        return userMapper.toAuthUser(user);
//    }

}
