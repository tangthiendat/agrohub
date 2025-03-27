package com.ttdat.debtservice.application.commands.payment;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class CmdPaymentDetail {
    String paymentDetailId;

    String debtAccountId;

    BigDecimal paymentAmount;
}
