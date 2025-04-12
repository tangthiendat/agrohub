package com.ttdat.customerservice.application.commands.customer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ttdat.core.domain.entities.CustomerType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class CreateCustomerCommand {
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
