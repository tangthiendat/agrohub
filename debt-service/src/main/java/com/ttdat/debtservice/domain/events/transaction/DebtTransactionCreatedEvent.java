package com.ttdat.debtservice.domain.events.transaction;

import com.ttdat.debtservice.domain.entities.DebtTransactionType;
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
public class DebtTransactionCreatedEvent {
    String debtTransactionId;

    String debtAccountId;

    String paymentMethodId;

    BigDecimal amount;

    DebtTransactionType transactionType;
}
