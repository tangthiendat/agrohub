package com.ttdat.userservice.api.dto.response;

import com.ttdat.userservice.domain.entities.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInfo {
    UUID userId;

    String fullName;

    LocalDate dob;

    String email;

    boolean active;

    Long warehouseId;

    Gender gender;

    String phoneNumber;

    RoleOption role;
}
