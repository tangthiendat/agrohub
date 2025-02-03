package com.ttdat.core.application.queries.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CheckPermissionQuery {
    String path;
    String httpMethod;

    @JsonCreator
    public CheckPermissionQuery(@JsonProperty("path") String path,
                                @JsonProperty("httpMethod") String httpMethod) {
        this.path = path;
        this.httpMethod = httpMethod;
    }
}
