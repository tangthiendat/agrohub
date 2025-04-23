package com.ttdat.debtservice.api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopCustomerDebtChartData {
    String customerName;
    BigDecimal totalDebt;
}
