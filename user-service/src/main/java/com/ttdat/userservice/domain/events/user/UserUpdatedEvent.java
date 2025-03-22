package com.ttdat.userservice.domain.events.user;

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
public class UserUpdatedEvent {
    String userId;

    String fullName;

    boolean active;

    Long warehouseId;

    Gender gender;

    String email;

    LocalDate dob;

    String phoneNumber;

    Long roleId;
}
