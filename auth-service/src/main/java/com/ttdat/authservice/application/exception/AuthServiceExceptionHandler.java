package com.ttdat.authservice.application.exception;

import com.ttdat.authservice.api.dto.response.ApiError;
import com.ttdat.authservice.api.dto.response.ApiResponse;
import com.ttdat.authservice.api.dto.response.ErrorDetail;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandExecutionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class AuthServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ApiError apiError = ApiError.builder()
                .errorCode(ErrorCode.EMAIL_NOT_FOUND.getCode())
                .errorType(ErrorCode.EMAIL_NOT_FOUND.getErrorType())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message(ex.getMessage())
                        .error(apiError)
                        .build());
    }

//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
//        ApiError apiError = ApiError.builder()
//                .errorCode(ex.getErrorCode().getCode())
//                .errorType(ex.getErrorCode().getErrorType())
//                .message(ex.getErrorCode().getMessage())
//                .build();
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(ApiResponse.builder()
//                        .status(HttpStatus.NOT_FOUND.value())
//                        .message(ex.getMessage())
//                        .error(apiError)
//                        .build());
//    }

    @ExceptionHandler(CommandExecutionException.class)
    public ResponseEntity<ApiResponse<Object>> handleCommandExecutionException(CommandExecutionException ex) {
        Optional<Object> exDetails = ex.getDetails();
        if (exDetails.isPresent()) {
            ApiResponse<Object> apiResponse = (ApiResponse<Object>) exDetails.get();
            return ResponseEntity.status(HttpStatus.valueOf(apiResponse.getStatus()))
                    .body(apiResponse);
        }
        return handleException(ex);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ErrorDetail> errorDetails = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> ErrorDetail.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());
        ApiError apiError = ApiError.builder()
                .errorCode(ErrorCode.INVALID_INPUT.getCode())
                .errorType(ErrorCode.INVALID_INPUT.getErrorType())
                .message(ErrorCode.INVALID_INPUT.getMessage())
                .errorDetails(errorDetails)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(ErrorCode.INVALID_INPUT.getMessage())
                        .error(apiError)
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        ApiError apiError = ApiError.builder()
                .errorCode(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                .errorType(ErrorCode.INTERNAL_SERVER_ERROR.getErrorType())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("An unexpected error occurred")
                        .error(apiError)
                        .build());
    }
}
