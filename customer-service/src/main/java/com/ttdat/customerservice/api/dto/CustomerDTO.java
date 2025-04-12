package com.ttdat.customerservice.api.dto;

import com.ttdat.core.domain.entities.CustomerType;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDTO {

    String customerId;

    @NotBlank(message = "Customer name is required")
    @Size(max = 150, message = "Customer name must not exceed 150 characters")
    String customerName;

    @NotNull(message = "Customer type is required")
    CustomerType customerType;

    @Email(message = "Invalid email format")
    String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10,20}$", message = "Phone number must be between 10 and 20 digits")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    String phoneNumber;

    boolean active;

    String address;

    @Size(max = 20, message = "Tax code must not exceed 20 characters")
    String taxCode;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    String notes;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
