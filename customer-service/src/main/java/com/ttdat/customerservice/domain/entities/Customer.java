package com.ttdat.customerservice.domain.entities;

import com.ttdat.core.domain.entities.CustomerType;
import com.ttdat.customerservice.infrastructure.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer extends Auditable {

    @Id
    @Column(length = 50)
    String customerId;

    @Column(length = 150, nullable = false)
    String customerName;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    CustomerType customerType;

    String email;

    @Column(length = 20, nullable = false)
    String phoneNumber;

    boolean active;

    String address;

    @Column(length = 20)
    String taxCode;

    @Column(length = 500)
    String notes;
}
