package com.ttdat.authservice.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ttdat.authservice.application.exception.ErrorType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    String errorCode;
    ErrorType errorType;
    String message;
    List<ErrorDetail> errorDetails;
}
