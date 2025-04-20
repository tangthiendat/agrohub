package com.ttdat.debtservice.application.commands.receipt;

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
public class CreateReceiptCommand {
    String receiptId;

    String customerId;

    Long warehouseId;

    String userId;

    LocalDate createdDate;

    BigDecimal totalReceivedAmount;

    String paymentMethodId;

    String note;

    List<CmdReceiptDetail> receiptDetails;
}