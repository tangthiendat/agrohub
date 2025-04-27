package com.ttdat.customerservice.domain.events.customer;

import com.ttdat.core.domain.entities.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CustomerUpdatedEvent {
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
