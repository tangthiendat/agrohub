package com.ttdat.userservice.api.dto.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionDTO {
    Long permissionId;
    @NotBlank(message = "Permission name is required")
    @Size(max = 100, message = "Permission name must not exceed 100 characters")
    String permissionName;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    String description;

    @NotBlank(message = "API path is required")
    @Size(max = 100, message = "API path must not exceed 100 characters")
    String apiPath;

    @NotBlank(message = "HTTP method is required")
    @Size(max = 10, message = "HTTP method must not exceed 10 characters")
    @Pattern(regexp = "^(GET|POST|PUT|DELETE|PATCH)$", message = "Invalid HTTP method")
    String httpMethod;

    @NotBlank(message = "Module is required")
    @Size(max = 50, message = "Module must not exceed 50 characters")
    String module;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
