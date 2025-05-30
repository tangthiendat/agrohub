package com.ttdat.userservice.api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleOption {
    Long roleId;
    String roleName;
    boolean active;
    String description;
}
