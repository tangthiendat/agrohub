package com.ttdat.core.api.dto.response;

import com.ttdat.core.domain.entities.Gender;
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
    String userId;

    String fullName;

    LocalDate dob;

    String email;

    boolean active;

    Long warehouseId;

    Gender gender;

    String phoneNumber;

    RoleInfo role;
}
