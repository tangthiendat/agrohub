package com.ttdat.debtservice.domain.entities;

import com.ttdat.debtservice.infrastructure.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment_methods")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentMethod extends Auditable {

    @Id
    @Column(length = 20)
    String paymentMethodId;

    @Column(length = 50)
    String paymentMethodName;

    @Column(length = 150)
    String description;

    @OneToMany(mappedBy = "paymentMethod")
    List<Payment> payments;
}
