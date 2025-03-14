package com.ttdat.userservice.domain.events.user;

import com.ttdat.core.domain.entities.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserCreatedEvent {
    UUID userId;
    String fullName;
    Gender gender;
    boolean active;
    Long warehouseId;
    String email;
    LocalDate dob;
    String password;
    String phoneNumber;
    Long roleId;
}
