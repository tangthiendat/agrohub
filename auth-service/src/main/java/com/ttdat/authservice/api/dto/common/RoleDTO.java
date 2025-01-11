package com.ttdat.authservice.api.dto.common;

import jakarta.validation.constraints.NotBlank;
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
    String roleName;

    boolean active;

    String description;

    List<PermissionDTO> permissions;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
