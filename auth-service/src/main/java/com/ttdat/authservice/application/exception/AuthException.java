package com.ttdat.authservice.application.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthException extends AuthenticationException {
    private final ErrorCode errorCode;

    public AuthException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
