package com.ttdat.debtservice.api.dto.common;

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
public class DebtAccountDTO {
    String debtAccountId;

    String sourceId;

    DebtSourceType sourceType;

    BigDecimal totalAmount;

    BigDecimal paidAmount;

    BigDecimal remainingAmount;

    Double interestRate;

    LocalDate dueDate;
}
