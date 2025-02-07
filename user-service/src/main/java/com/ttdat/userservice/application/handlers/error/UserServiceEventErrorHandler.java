package com.ttdat.userservice.application.handlers.error;

import com.ttdat.core.api.dto.response.ApiError;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.application.exceptions.DuplicateResourceException;
import com.ttdat.core.application.exceptions.ResourceInUseException;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.EventMessageHandler;
import org.axonframework.eventhandling.ListenerInvocationErrorHandler;
import org.springframework.http.HttpStatus;

import javax.annotation.Nonnull;

@Slf4j
public class UserServiceEventErrorHandler implements ListenerInvocationErrorHandler{
    @Override
    public void onError(@Nonnull Exception exception, @Nonnull EventMessage<?> event, @Nonnull EventMessageHandler eventHandler) {
        if(exception instanceof ResourceNotFoundException) {
            ResourceNotFoundException resourceNotFoundException = (ResourceNotFoundException) exception;
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
        if(exception instanceof DuplicateResourceException){
            DuplicateResourceException duplicateResourceException = (DuplicateResourceException) exception;
            ApiError apiError = ApiError.builder()
                    .errorCode(duplicateResourceException.getErrorCode().getCode())
                    .errorType(duplicateResourceException.getErrorCode().getErrorType())
                    .message("Resource already exists")
                    .build();
            ApiResponse<Object> apiResponse = ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(duplicateResourceException.getMessage())
                    .error(apiError)
                    .build();
            throw new CommandExecutionException("Resource already exists", exception, apiResponse);
        }
        if(exception instanceof ResourceInUseException){
            ResourceInUseException resourceInUseException = (ResourceInUseException) exception;
            ApiError apiError = ApiError.builder()
                    .errorCode(resourceInUseException.getErrorCode().getCode())
                    .errorType(resourceInUseException.getErrorCode().getErrorType())
                    .message(resourceInUseException.getErrorCode().getMessage())
                    .build();
            ApiResponse<Object> apiResponse = ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Resource in use")
                    .error(apiError)
                    .build();
            throw new CommandExecutionException("Resource in use", exception, apiResponse);
        }
    }
}
