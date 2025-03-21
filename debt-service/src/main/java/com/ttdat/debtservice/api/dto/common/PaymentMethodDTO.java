package com.ttdat.debtservice.api.dto.common;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentMethodDTO {
    String paymentMethodId;

    String paymentMethodName;

    String description;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
