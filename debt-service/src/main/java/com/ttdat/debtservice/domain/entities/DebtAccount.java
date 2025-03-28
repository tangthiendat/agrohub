package com.ttdat.debtservice.domain.entities;

import com.ttdat.core.domain.entities.DebtPartyType;
import com.ttdat.core.domain.entities.DebtSourceType;
import com.ttdat.core.domain.entities.DebtStatus;
import com.ttdat.debtservice.infrastructure.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "debt_accounts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DebtAccount extends Auditable {
    @Id
    @Column(length = 50)
    String debtAccountId;

    @Column(length = 50, nullable = false)
    String partyId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    DebtPartyType partyType;

    @Column(length = 50, nullable = false)
    String sourceId;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    DebtSourceType sourceType;

    @Column(precision = 15, scale = 2, nullable = false)
    BigDecimal totalAmount;

    @Column(precision = 15, scale = 2, nullable = false)
    BigDecimal paidAmount;

    @Column(precision = 15, scale = 2, nullable = false)
    BigDecimal remainingAmount;

    @Column(precision = 2, nullable = false)
    Double interestRate;

    @Column(nullable = false)
    LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    DebtStatus debtStatus;

    @OneToMany(mappedBy = "debtAccount", fetch = FetchType.EAGER)
    List<DebtTransaction> debtTransactions;
}
