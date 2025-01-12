package com.ttdat.authservice.application.commands.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ttdat.authservice.domain.entities.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CreateUserCommand {
    String fullName;
    Gender gender;
    boolean active;
    String email;
    String password;
    String phoneNumber;
    Long roleId;

    @JsonCreator
    public CreateUserCommand(@JsonProperty("fullName") String fullName,
                             @JsonProperty("gender") Gender gender,
                             @JsonProperty("active") boolean active,
                             @JsonProperty("email") String email,
                             @JsonProperty("password") String password,
                             @JsonProperty("phoneNumber") String phoneNumber,
                             @JsonProperty("roleId") Long roleId) {
        this.fullName = fullName;
        this.active = active;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.roleId = roleId;
    }

}
