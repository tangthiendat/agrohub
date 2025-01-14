package com.ttdat.authservice.application.commands.role;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CreateRoleCommand {
    String roleName;
    boolean active;
    String description;
    List<Long> permissionIds;

    @JsonCreator
    public CreateRoleCommand(
            @JsonProperty("roleName") String roleName,
            @JsonProperty("active") boolean active,
            @JsonProperty("description") String description,
            @JsonProperty("permissionIds") List<Long> permissionIds){
        this.roleName = roleName;
        this.active = active;
        this.description = description;
        this.permissionIds = permissionIds;
    }
}
