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

    @Column(length = 50)
    String partyId;

    @Enumerated(EnumType.STRING)
    DebtPartyType partyType;

    @Column(length = 50)
    String sourceId;

    @Enumerated(EnumType.STRING)
    DebtSourceType sourceType;

    BigDecimal totalAmount;

    BigDecimal paidAmount;

    BigDecimal remainingAmount;

    @Column(precision = 2)
    Double interestRate;

    LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    DebtStatus debtStatus;

    @OneToMany(mappedBy = "debtAccount")
    List<DebtTransaction> debtTransactions;
}
