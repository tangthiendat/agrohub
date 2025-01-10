package com.ttdat.authservice.application.commands.user;

import com.ttdat.authservice.domain.entities.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CreateUserCommand {
    String fullName;
    Gender gender;
    String email;
    String password;
    String phoneNumber;
    Long roleId;
}
