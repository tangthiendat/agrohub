package com.ttdat.userservice.api.dto.common;

import com.ttdat.userservice.domain.entities.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    UUID userId;

    String fullName;

    LocalDate dob;

    String email;

    boolean active;

    Gender gender;

    String password;

    String phoneNumber;

    RoleDTO role;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
