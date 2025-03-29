package com.ttdat.userservice.application.commands.user;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ttdat.core.domain.entities.Gender;
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
public class UpdateUserCommand {
    String userId;

    String fullName;

    Gender gender;

    boolean active;

    Long warehouseId;

    String email;

    LocalDate dob;

    String phoneNumber;

    Long roleId;
}
