package com.ttdat.purchaseservice.domain.events;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierCreatedEvent {
    String supplierId;

    String supplierName;

    String email;

    String phoneNumber;

    boolean active;

    String address;

    String taxCode;

    String contactPerson;

    String notes;
}
