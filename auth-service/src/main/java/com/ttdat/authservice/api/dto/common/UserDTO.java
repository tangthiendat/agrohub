package com.ttdat.authservice.api.dto.common;

import com.ttdat.authservice.domain.entities.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    UUID userId;
    String fullName;
    String email;
    Gender gender;
    String password;
    String phoneNumber;
    RoleDTO role;
}
