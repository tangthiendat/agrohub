package com.ttdat.debtservice.api.dto.common;

import com.ttdat.debtservice.domain.entities.DebtTransactionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DebtTransactionDTO {
    String debtTransactionId;

    BigDecimal amount;

    String sourceId;

    DebtTransactionType transactionType;

    LocalDateTime createdAt;
}
