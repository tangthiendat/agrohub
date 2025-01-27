package com.ttdat.authservice.application.commands.permission;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CreatePermissionCommand {
    String permissionName;
    String description;
    String apiPath;
    String httpMethod;
    String module;

    @JsonCreator
    public CreatePermissionCommand(@JsonProperty("permissionName") String permissionName,
                                   @JsonProperty("description") String description,
                                   @JsonProperty("apiPath") String apiPath,
                                   @JsonProperty("httpMethod") String httpMethod,
                                   @JsonProperty("module") String module) {
        this.permissionName = permissionName;
        this.description = description;
        this.apiPath = apiPath;
        this.httpMethod = httpMethod;
        this.module = module;
    }
}
