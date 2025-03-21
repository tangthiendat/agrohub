package com.ttdat.debtservice.api.dto.common;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentMethodDTO {
    String paymentMethodId;

    String paymentMethodName;

    String description;
}
