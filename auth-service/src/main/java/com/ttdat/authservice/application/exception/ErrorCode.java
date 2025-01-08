package com.ttdat.authservice.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    EMAIL_NOT_FOUND("USR-001", "User email not found"),

    PERMISSION_NOT_FOUND("PER-001", "Permission not found"),;

    private final String code;
    private final String message;
}
