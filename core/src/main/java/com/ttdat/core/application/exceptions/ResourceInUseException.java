package com.ttdat.core.application.exceptions;

import lombok.Getter;

@Getter
public class ResourceInUseException extends RuntimeException {
    private final ErrorCode errorCode;

    public ResourceInUseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
