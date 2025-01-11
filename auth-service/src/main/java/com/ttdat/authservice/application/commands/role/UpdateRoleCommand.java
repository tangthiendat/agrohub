package com.ttdat.authservice.application.commands.role;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UpdateRoleCommand {
    Long roleId;
    String roleName;
    boolean active;
    String description;
    List<Long> permissionIds;

    @JsonCreator
    public UpdateRoleCommand(@JsonProperty("roleId") Long roleId,
                             @JsonProperty("roleName") String roleName,
                             @JsonProperty("active") boolean active,
                             @JsonProperty("description") String description,
                             @JsonProperty("permissionIds") List<Long> permissionIds) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.active = active;
        this.description = description;
        this.permissionIds = permissionIds;
    }
}
