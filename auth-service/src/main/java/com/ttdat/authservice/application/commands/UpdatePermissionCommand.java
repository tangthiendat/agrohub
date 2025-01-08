package com.ttdat.authservice.application.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UpdatePermissionCommand {
    Long permissionId;
    String permissionName;
    String description;
    String apiPath;
    String httpMethod;
    String module;
}
