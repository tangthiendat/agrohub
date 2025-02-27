package com.ttdat.userservice.application.commands.user;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ttdat.userservice.domain.entities.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class CreateUserCommand {
    String fullName;
    Gender gender;
    boolean active;
    String email;
    Long warehouseId;
    LocalDate dob;
    String password;
    String phoneNumber;
    Long roleId;
}
