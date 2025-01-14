package com.ttdat.authservice.api.dto.response;

import com.ttdat.authservice.application.exception.ErrorType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiError {
    String errorCode;
    ErrorType errorType;
    String message;
    List<ErrorDetail> errorDetails;
}
