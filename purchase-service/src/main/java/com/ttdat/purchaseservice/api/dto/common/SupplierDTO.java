package com.ttdat.purchaseservice.api.dto.common;

import com.ttdat.purchaseservice.api.dto.response.SupplierRatingDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierDTO {

    String supplierId;

    @NotBlank(message = "Supplier name is required")
    @Size(max = 150, message = "Supplier name must not exceed 150 characters")
    String supplierName;

    @NotBlank(message = "Supplier email is required")
    @Email(message = "Invalid email format")
    String email;

    @NotBlank(message = "Supplier phone number is required")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    @Pattern(regexp = "^[0-9]+$", message = "Phone number must contain only digits")
    String phoneNumber;

    boolean active;

    String address;

    @Size(max = 20, message = "Tax code must not exceed 20 characters")
    String taxCode;

    @Size(max = 100, message = "Contact person must not exceed 100 characters")
    String contactPerson;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    String notes;

    SupplierRatingDTO supplierRating;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
