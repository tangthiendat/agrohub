package com.ttdat.core.application.commands.debt;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ttdat.core.domain.entities.DebtPartyType;
import com.ttdat.core.domain.entities.DebtSourceType;
import com.ttdat.core.domain.entities.DebtStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class CreateDebtAccountCommand {
    @TargetAggregateIdentifier
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
