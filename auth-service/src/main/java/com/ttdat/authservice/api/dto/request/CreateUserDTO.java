package com.ttdat.authservice.api.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ttdat.authservice.domain.entities.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateUserDTO {
    String fullName;
    Gender gender;
    String email;
    String password;
    String phoneNumber;
    Long roleId;
}
