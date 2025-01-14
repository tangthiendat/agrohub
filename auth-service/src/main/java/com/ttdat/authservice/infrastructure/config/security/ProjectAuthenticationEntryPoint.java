package com.ttdat.authservice.infrastructure.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttdat.authservice.api.dto.response.ApiError;
import com.ttdat.authservice.api.dto.response.ApiResponse;
import com.ttdat.authservice.application.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ProjectAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();
    private final ObjectMapper mapper;

    public ProjectAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        delegate.commence(request, response, authException);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ApiError error = ApiError.builder()
                .errorCode(ErrorCode.UNAUTHORIZED.getCode())
                .errorType(ErrorCode.UNAUTHORIZED.getErrorType())
                .message(authException.getMessage())
                .build();
        ApiResponse<Object> res = ApiResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .success(false)
                .message(ErrorCode.UNAUTHORIZED.getMessage())
                .error(error)
                .build();
        mapper.writeValue(response.getWriter(), res);
    }
}
