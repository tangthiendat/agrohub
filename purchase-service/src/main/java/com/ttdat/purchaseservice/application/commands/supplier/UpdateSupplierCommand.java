package com.ttdat.purchaseservice.application.commands.supplier;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class UpdateSupplierCommand {
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
