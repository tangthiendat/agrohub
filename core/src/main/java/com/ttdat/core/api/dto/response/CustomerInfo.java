package com.ttdat.core.api.dto.response;

import com.ttdat.core.domain.entities.CustomerType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerInfo {
    String customerId;

    String customerName;

    CustomerType customerType;

    String email;

    String phoneNumber;

    boolean active;

    String address;

    String taxCode;

    String notes;
}
