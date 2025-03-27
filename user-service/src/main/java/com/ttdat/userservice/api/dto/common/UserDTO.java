package com.ttdat.userservice.api.dto.common;

import com.ttdat.core.domain.entities.Gender;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    String fullName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    LocalDate dob;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email;

    boolean active;

    @NotNull(message = "Warehouse ID is required")
    Long warehouseId;

    @NotNull(message = "Gender is required")
    @Pattern(regexp = "^(MALE|FEMALE|OTHER)$", message = "Invalid gender")
    Gender gender;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must contain at least 8 characters, one uppercase, one lowercase, one number and one special character")
    String password;

    @NotBlank(message = "Phone number is required")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
    String phoneNumber;

    @NotNull(message = "Role is required")
    RoleDTO role;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
