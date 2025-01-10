package com.ttdat.authservice.application.commands.role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CreateRoleCommand {
    String roleName;
    boolean active;
    String description;
    List<Long> permissionIds;
}
