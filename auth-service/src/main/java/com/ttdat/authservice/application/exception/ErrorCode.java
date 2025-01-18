package com.ttdat.authservice.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_INPUT("AGH-101", "Invalid input", ErrorType.VALIDATION),

    UNAUTHORIZED("AGH-201", "Unauthorized", ErrorType.AUTHENTICATION),
    ACCOUNT_DISABLED("AGH-202", "Account is disabled", ErrorType.AUTHENTICATION),
    INVALID_CREDENTIALS("AGH-203", "Invalid credentials", ErrorType.AUTHENTICATION),
    TOKEN_NOT_VALID("AGH-204", "Token not valid", ErrorType.AUTHENTICATION),
    ACCESS_DENIED("AGH-205", "You do not have permission to perform this action", ErrorType.AUTHENTICATION),

    EMAIL_NOT_FOUND("AGH-301", "User email not found", ErrorType.RESOURCE),
    USER_NOT_FOUND("AGH-302", "User not found", ErrorType.RESOURCE),
    ROLE_NOT_FOUND("AGH-303", "Role not found", ErrorType.RESOURCE),
    PERMISSION_NOT_FOUND("AGH-304", "Permission not found", ErrorType.RESOURCE),

    INTERNAL_SERVER_ERROR("AGH-501", "Internal server error", ErrorType.SYSTEM),;

    private final String code;
    private final String message;
    private final ErrorType errorType;
}
