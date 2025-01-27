package com.ttdat.authservice.application.commands.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UpdateUserStatusCommand {
    UUID userId;
    boolean active;

    @JsonCreator
    public UpdateUserStatusCommand(@JsonProperty("userId") UUID userId,
                                   @JsonProperty("active") boolean active) {
        this.userId = userId;
        this.active = active;
    }
}
