package com.ttdat.authservice.domain.events.role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RoleUpdatedEvent {
    Long roleId;
    String roleName;
    boolean active;
    String description;
    List<Long> permissionIds;
}
