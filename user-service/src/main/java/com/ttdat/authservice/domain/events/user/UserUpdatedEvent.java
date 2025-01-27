package com.ttdat.authservice.domain.events.user;

import com.ttdat.authservice.domain.entities.Gender;
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
public class UserUpdatedEvent {
    UUID userId;
    String fullName;
    boolean active;
    Gender gender;
    String email;
    LocalDate dob;
    String phoneNumber;
    Long roleId;
}
