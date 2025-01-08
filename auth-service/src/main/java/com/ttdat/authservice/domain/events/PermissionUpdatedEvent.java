package com.ttdat.authservice.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class PermissionUpdatedEvent {
    Long permissionId;
    String permissionName;
    String description;
    String apiPath;
    String httpMethod;
    String module;
}
