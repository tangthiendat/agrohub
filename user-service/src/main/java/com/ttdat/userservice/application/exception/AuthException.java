package com.ttdat.userservice.application.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {
    private final ErrorCode errorCode;

    public AuthException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
