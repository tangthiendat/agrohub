package com.ttdat.authservice.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermissionDTO {
    Long permissionId;
    @NotBlank(message = "Permission name is required")
    String permissionName;

    String description;
    @NotBlank(message = "API path is required")
    String apiPath;

    @NotBlank(message = "HTTP method is required")
    String httpMethod;

    @NotBlank(message = "Module is required")
    String module;
}
