package com.ttdat.userservice.application.commands.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ttdat.userservice.domain.entities.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CreateUserCommand {
    String fullName;
    Gender gender;
    boolean active;
    String email;
    LocalDate dob;
    String password;
    String phoneNumber;
    Long roleId;

    @JsonCreator
    public CreateUserCommand(@JsonProperty("fullName") String fullName,
                             @JsonProperty("gender") Gender gender,
                             @JsonProperty("active") boolean active,
                             @JsonProperty("email") String email,
                             @JsonProperty("dob") LocalDate dob,
                             @JsonProperty("password") String password,
                             @JsonProperty("phoneNumber") String phoneNumber,
                             @JsonProperty("roleId") Long roleId) {
        this.fullName = fullName;
        this.active = active;
        this.gender = gender;
        this.email = email;
        this.dob = dob;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.roleId = roleId;
    }

}
