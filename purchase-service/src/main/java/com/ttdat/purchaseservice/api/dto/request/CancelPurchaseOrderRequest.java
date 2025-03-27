package com.ttdat.purchaseservice.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CancelPurchaseOrderRequest {
    @NotBlank(message = "Cancel reason is required")
    @Size(max = 255, message = "Cancel reason must not exceed 255 characters")
    String cancelReason;
}
