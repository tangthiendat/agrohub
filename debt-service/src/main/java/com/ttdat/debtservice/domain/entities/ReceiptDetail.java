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
@Table(name = "receipt_details", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"receipt_id", "debt_account_id"})
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReceiptDetail {
    @Id
    @Column(length = 50)
    String receiptDetailId;

    @ManyToOne
    @JoinColumn(name = "receipt_id", nullable = false)
    Receipt receipt;

    @ManyToOne
    @JoinColumn(name = "debt_account_id", nullable = false)
    DebtAccount debtAccount;

    @Column(precision = 15, scale = 2)
    BigDecimal receiptAmount;
}
