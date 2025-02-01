package com.ttdat.userservice.application.queries.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class GetUserByIdQuery {
    String userId;

    @JsonCreator
    public GetUserByIdQuery(@JsonProperty("userId") String userId) {
        this.userId = userId;
    }
}
