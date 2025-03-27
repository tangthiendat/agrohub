package com.ttdat.debtservice.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment_details", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"payment_id", "debt_account_id"})
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDetail {
    @Id
    @Column(length = 50)
    String paymentDetailId;

    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false)
    Payment payment;

    @ManyToOne
    @JoinColumn(name = "debt_account_id", nullable = false)
    DebtAccount debtAccount;

    @Column(precision = 15, scale = 2)
    BigDecimal paymentAmount;
}
