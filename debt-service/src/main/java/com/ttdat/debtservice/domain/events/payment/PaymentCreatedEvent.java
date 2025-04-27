package com.ttdat.debtservice.domain.events.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class PaymentCreatedEvent {
    String paymentId;

    String supplierId;

    Long warehouseId;

    String userId;

    LocalDate createdDate;

    BigDecimal totalPaidAmount;

    String paymentMethodId;

    String note;

    List<EvtPaymentDetail> paymentDetails;
}
