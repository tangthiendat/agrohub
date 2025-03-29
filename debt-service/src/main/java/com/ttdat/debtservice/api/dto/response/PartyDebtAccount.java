package com.ttdat.debtservice.api.dto.response;

import com.ttdat.core.domain.entities.DebtSourceType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PartyDebtAccount {
    String debtAccountId;

    String sourceId;

    DebtSourceType sourceType;

    BigDecimal totalAmount;

    BigDecimal paidAmount;

    BigDecimal remainingAmount;

    BigDecimal interestRate;

    LocalDate dueDate;
}
