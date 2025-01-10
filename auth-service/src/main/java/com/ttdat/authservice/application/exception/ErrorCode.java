package com.ttdat.authservice.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("ERR-500", "Internal server error", ErrorType.SYSTEM),
    INVALID_INPUT("ERR-400", "Invalid input", ErrorType.VALIDATION),

    EMAIL_NOT_FOUND("USR-001", "User email not found", ErrorType.AUTHENTICATION),

    PERMISSION_NOT_FOUND("PER-001", "Permission not found", ErrorType.RESOURCE),;

    private final String code;
    private final String message;
    private final ErrorType errorType;
}
