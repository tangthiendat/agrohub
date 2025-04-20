package com.ttdat.debtservice.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateReceiptRequest {
    @NotBlank(message = "Customer ID is required")
    @Size(max = 50, message = "Customer ID must not exceed 50 characters")
    String customerId;

    @NotNull(message = "Warehouse ID is required")
    Long warehouseId;

    @NotBlank(message = "User ID is required")
    @Size(max = 50, message = "User ID must not exceed 50 characters")
    String userId;

    @NotNull(message = "Created date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate createdDate;

    @NotNull(message = "Total received amount is required")
    @DecimalMin(value = "0.01", message = "Total received amount must be greater than 0")
    @Digits(integer = 15, fraction = 2, message = "Total received amount must have at most 15 digits and 2 decimal places")
    BigDecimal totalReceivedAmount;

    @NotBlank(message = "Payment method ID is required")
    String paymentMethodId;

    @Size(max = 255, message = "Note must not exceed 255 characters")
    String note;

    @NotEmpty(message = "Receipt details are required")
    @Valid
    List<CreateReceiptDetailRequest> receiptDetails;
}