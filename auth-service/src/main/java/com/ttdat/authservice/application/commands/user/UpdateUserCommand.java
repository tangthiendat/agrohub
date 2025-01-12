package com.ttdat.authservice.application.commands.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ttdat.authservice.domain.entities.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UpdateUserCommand {
    UUID userId;
    String fullName;
    Gender gender;
    boolean active;
    String email;
    String phoneNumber;
    Long roleId;

    @JsonCreator
    public UpdateUserCommand(@JsonProperty("userId") UUID userId,
                             @JsonProperty("fullName") String fullName,
                             @JsonProperty("gender") Gender gender,
                             @JsonProperty("active") boolean active,
                             @JsonProperty("email") String email,
                             @JsonProperty("phoneNumber") String phoneNumber,
                             @JsonProperty("roleId") Long roleId) {
        this.userId = userId;
        this.fullName = fullName;
        this.active = active;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.roleId = roleId;
    }

}
