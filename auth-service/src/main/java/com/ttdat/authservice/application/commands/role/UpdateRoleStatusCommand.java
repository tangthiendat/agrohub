package com.ttdat.authservice.application.commands.role;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UpdateRoleStatusCommand {
    Long roleId;
    boolean active;

    @JsonCreator
    public UpdateRoleStatusCommand(@JsonProperty("roleId") Long roleId,
                                   @JsonProperty("active") boolean active) {
        this.roleId = roleId;
        this.active = active;
    }
}
