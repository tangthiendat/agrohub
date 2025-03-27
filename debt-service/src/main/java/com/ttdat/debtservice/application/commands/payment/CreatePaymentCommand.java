package com.ttdat.debtservice.application.commands.payment;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class CreatePaymentCommand {
    String paymentId;

    String supplierId;

    Long warehouseId;

    String userId;

    LocalDate createdDate;

    BigDecimal totalPaidAmount;

    String paymentMethodId;

    String note;

    List<CmdPaymentDetail> paymentDetails;
}
