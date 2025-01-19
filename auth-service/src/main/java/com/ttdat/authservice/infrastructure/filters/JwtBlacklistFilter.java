package com.ttdat.authservice.infrastructure.filters;

import com.ttdat.authservice.application.exception.AuthException;
import com.ttdat.authservice.application.exception.ErrorCode;
import com.ttdat.authservice.application.queries.auth.IsTokenBlacklistedQuery;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtBlacklistFilter extends OncePerRequestFilter {
    private final QueryGateway queryGateway;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException, AuthException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            IsTokenBlacklistedQuery isTokenBlacklistedQuery = IsTokenBlacklistedQuery.builder()
                    .token(token)
                    .build();
            boolean isBlacklisted = queryGateway.query(isTokenBlacklistedQuery, ResponseTypes.instanceOf(Boolean.class)).join();
            if(isBlacklisted) {
                throw new AuthException(ErrorCode.TOKEN_NOT_VALID);
            }
        }
        filterChain.doFilter(request, response);
    }
}
