package com.ttdat.authservice.application.queryhandlers;

import com.ttdat.authservice.application.exception.ErrorCode;
import com.ttdat.authservice.application.exception.ResourceNotFoundException;
import com.ttdat.authservice.application.queries.auth.CheckPermissionQuery;
import com.ttdat.authservice.application.queries.auth.GetAuthenticationByIdQuery;
import com.ttdat.authservice.application.queries.auth.IsTokenBlacklistedQuery;
import com.ttdat.authservice.domain.entities.Role;
import com.ttdat.authservice.domain.entities.User;
import com.ttdat.authservice.domain.repositories.UserRepository;
import com.ttdat.authservice.domain.services.TokenBlacklistService;
import com.ttdat.authservice.infrastructure.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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
        String email = authentication.getName();
        if (email != null) {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                Role role = user.getRole();
                if (role != null) {
                    return role.getPermissions().stream()
                            .anyMatch(permission -> permission.getApiPath().equals(checkPermissionQuery.getPath())
                                    && permission.getHttpMethod().equals(checkPermissionQuery.getHttpMethod())) && role.isActive();
                }
            }
        }
        return false;
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
