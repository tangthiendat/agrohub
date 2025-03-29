package com.ttdat.debtservice.domain.events.debt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class DebtAccountAmountUpdatedEvent {
    String debtAccountId;

    BigDecimal paidAmount;

    String transactionSourceId;
}
