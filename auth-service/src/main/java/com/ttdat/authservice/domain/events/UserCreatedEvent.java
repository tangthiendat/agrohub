package com.ttdat.authservice.domain.events;

import com.ttdat.authservice.domain.entities.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

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
    String email;
    String password;
    String phoneNumber;
    Long roleId;
}
