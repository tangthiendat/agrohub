package com.ttdat.core.application.queries.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetAuthenticationByIdQuery {
    String userId;

    @JsonCreator
    public GetAuthenticationByIdQuery(@JsonProperty("userId") String userId) {
        this.userId = userId;
    }
}
