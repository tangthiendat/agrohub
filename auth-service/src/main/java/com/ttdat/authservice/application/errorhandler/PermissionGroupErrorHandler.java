package com.ttdat.authservice.application.errorhandler;

import com.ttdat.authservice.api.dto.response.ApiError;
import com.ttdat.authservice.api.dto.response.ApiResponse;
import com.ttdat.authservice.application.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.EventMessageHandler;
import org.axonframework.eventhandling.ListenerInvocationErrorHandler;
import org.springframework.http.HttpStatus;

import javax.annotation.Nonnull;

@Slf4j
public class PermissionGroupErrorHandler implements ListenerInvocationErrorHandler {
    @Override
    public void onError(@Nonnull Exception exception, @Nonnull EventMessage<?> event, @Nonnull EventMessageHandler eventHandler) throws Exception {
        if(exception instanceof ResourceNotFoundException) {
            ResourceNotFoundException resourceNotFoundException = (ResourceNotFoundException) exception;
            log.error("Resource not found exception message: {}", exception.getMessage());
            ApiError apiError = ApiError.builder()
                    .errorCode(resourceNotFoundException.getErrorCode().getCode())
                    .errorType(resourceNotFoundException.getErrorCode().getErrorType())
                    .message(resourceNotFoundException.getErrorCode().getMessage())
                    .build();
            ApiResponse<Object> apiResponse = ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(resourceNotFoundException.getMessage())
                    .error(apiError)
                    .build();
            throw new CommandExecutionException("Resource not found", exception, apiResponse);
        }

    }
}
