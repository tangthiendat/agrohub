package com.ttdat.authservice.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ttdat.authservice.domain.entities.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    UUID userId;
    String fullName;
    String email;
    Gender gender;
    String phoneNumber;
    RoleDTO role;
}
