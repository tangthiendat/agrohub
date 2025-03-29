package com.ttdat.debtservice.api.dto.common;

import com.ttdat.core.domain.entities.DebtSourceType;
import com.ttdat.core.domain.entities.DebtStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DebtAccountDTO {
    String debtAccountId;

    String partyId;

    String sourceId;

    DebtSourceType sourceType;

    BigDecimal totalAmount;

    BigDecimal paidAmount;

    BigDecimal remainingAmount;

    BigDecimal interestRate;

    LocalDate dueDate;

    DebtStatus debtStatus;

    List<DebtTransactionDTO> debtTransactions;
}
