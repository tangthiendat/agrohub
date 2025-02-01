package com.ttdat.userservice.application.commands.permission;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class DeletePermissionCommand {
    Long permissionId;

    @JsonCreator
    public DeletePermissionCommand(@JsonProperty("permissionId") Long permissionId) {
        this.permissionId = permissionId;
    }

}
