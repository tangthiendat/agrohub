package com.ttdat.debtservice.domain.events.receipt;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EvtReceiptDetail {
    String receiptDetailId;

    String debtAccountId;

    BigDecimal receiptAmount;
}