package com.ttdat.debtservice.application.commands.transaction;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ttdat.debtservice.domain.entities.DebtTransactionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class CreateDebtTransactionCommand {
    String debtTransactionId;

    String debtAccountId;

    BigDecimal amount;

    String sourceId;

    DebtTransactionType transactionType;
}
