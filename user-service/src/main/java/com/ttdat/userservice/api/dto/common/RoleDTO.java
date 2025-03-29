package com.ttdat.userservice.api.dto.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDTO {

    Long roleId;


    @NotBlank(message = "Role name is required")
    @Size(max = 50, message = "Role name must not exceed 50 characters")
    String roleName;

    boolean active;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    String description;

    List<PermissionDTO> permissions;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
