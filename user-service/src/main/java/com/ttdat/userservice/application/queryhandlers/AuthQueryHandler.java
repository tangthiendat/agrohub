package com.ttdat.userservice.application.queryhandlers;

import com.ttdat.userservice.application.exception.ErrorCode;
import com.ttdat.userservice.application.exception.ResourceNotFoundException;
import com.ttdat.userservice.application.queries.auth.CheckPermissionQuery;
import com.ttdat.userservice.application.queries.auth.GetAuthenticationByIdQuery;
import com.ttdat.userservice.application.queries.auth.IsTokenBlacklistedQuery;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class AuthQueryHandler {
    private final UserRepository userRepository;
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtUtils jwtUtils;

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

}
