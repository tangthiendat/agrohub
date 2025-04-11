package com.ttdat.debtservice.domain.events.debt;

import com.ttdat.core.domain.entities.DebtPartyType;
import com.ttdat.core.domain.entities.DebtSourceType;
import com.ttdat.core.domain.entities.DebtStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class DebtAccountCreatedEvent {
    String debtAccountId;

    String partyId;

    DebtPartyType partyType;

    String sourceId;

    DebtSourceType sourceType;

    BigDecimal totalAmount;

    BigDecimal paidAmount;

    BigDecimal remainingAmount;

    Double interestRate;

    LocalDate dueDate;

    DebtStatus debtStatus;
}
