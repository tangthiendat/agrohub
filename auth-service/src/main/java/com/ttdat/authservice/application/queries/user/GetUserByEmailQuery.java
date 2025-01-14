package com.ttdat.authservice.application.queries.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class GetUserByEmailQuery {
    String email;

    @JsonCreator
    public GetUserByEmailQuery(@JsonProperty("email") String email) {
        this.email = email;
    }
}
