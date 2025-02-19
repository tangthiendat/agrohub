package com.ttdat.purchaseservice.api.dto.common;

import jakarta.validation.constraints.NotBlank;
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
    String supplierName;

    @NotBlank(message = "Supplier email is required")
    String email;

    @NotBlank(message = "Supplier phone number is required")
    String phoneNumber;

    boolean active;

    String address;

    String taxCode;

    String contactPerson;

    String notes;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
