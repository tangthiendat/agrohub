package com.ttdat.core.infrastructure.utils;

public class Validator {

    private Validator() {
    }

    public static <T> void validateArgument(T value, ValidationCallback<T> validationCallback, String errorMessage) {
        if (!validationCallback.validate(value)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static <T> void validateState(T value, ValidationCallback<T> validationCallback, String errorMessage) {
        if (!validationCallback.validate(value)) {
            throw new IllegalStateException(errorMessage);
        }
    }
}
