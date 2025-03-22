package com.ttdat.debtservice.domain.entities;

import com.ttdat.debtservice.infrastructure.audit.Auditable;
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
@Table(name = "debt_transactions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DebtTransaction extends Auditable {

    @Id
    @Column(length = 20)
    String debtTransactionId;

    @ManyToOne
    @JoinColumn(name = "debt_account_id")
    DebtAccount debtAccount;

    @Column(precision = 15, scale = 2)
    BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    DebtTransactionType transactionType;
}
