package com.ttdat.authservice.infrastructure.filters;

import com.ttdat.authservice.application.exception.AuthException;
import com.ttdat.authservice.application.exception.ErrorCode;
import com.ttdat.authservice.domain.services.TokenBlacklistService;
import com.ttdat.authservice.infrastructure.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtBlacklistFilter extends OncePerRequestFilter {
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, AuthException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String tokenId = jwtUtils.getTokenId(token);
            if(tokenBlacklistService.isBlacklisted(tokenId)) {
                throw new AuthException(ErrorCode.TOKEN_NOT_VALID);
            }
        }
        filterChain.doFilter(request, response);
    }
}
