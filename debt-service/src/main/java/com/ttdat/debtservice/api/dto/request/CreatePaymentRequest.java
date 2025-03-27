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
public class CreatePaymentRequest {
    @NotBlank(message = "Supplier ID is required")
    @Size(max = 50, message = "Supplier ID must not exceed 50 characters")
    String supplierId;

    @NotNull(message = "Warehouse ID is required")
    Long warehouseId;

    @NotBlank(message = "User ID is required")
    @Size(max = 50, message = "User ID must not exceed 50 characters")
    String userId;

    @NotNull(message = "Created date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate createdDate;

    @NotNull(message = "Total paid amount is required")
    @DecimalMin(value = "0.01", message = "Total paid amount must be greater than 0")
    @Digits(integer = 15, fraction = 2, message = "Total paid amount must have at most 15 digits and 2 decimal places")
    BigDecimal totalPaidAmount;

    @NotBlank(message = "Payment method ID is required")
    String paymentMethodId;

    @Size(max = 255, message = "Note must not exceed 255 characters")
    String note;

    @NotEmpty(message = "Payment details are required")
    @Valid
    List<CreatePaymentDetailRequest> paymentDetails;
}