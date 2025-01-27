package com.ttdat.authservice.application.queries.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class GetAuthenticationByIdQuery {
    String userId;

    @JsonCreator
    public GetAuthenticationByIdQuery(@JsonProperty("userId") String userId) {
        this.userId = userId;
    }
}
