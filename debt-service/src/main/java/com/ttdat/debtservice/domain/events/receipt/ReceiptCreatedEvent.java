package com.ttdat.debtservice.domain.events.receipt;

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
public class ReceiptCreatedEvent {
    String receiptId;

    String customerId;

    Long warehouseId;

    String userId;

    LocalDate createdDate;

    BigDecimal totalReceivedAmount;

    String paymentMethodId;

    String note;

    List<EvtReceiptDetail> receiptDetails;
}