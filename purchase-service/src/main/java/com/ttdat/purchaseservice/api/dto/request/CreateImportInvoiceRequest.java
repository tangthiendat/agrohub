package com.ttdat.purchaseservice.api.dto.request;

import com.ttdat.core.domain.entities.DiscountType;
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
public class CreateImportInvoiceRequest {
    @NotNull(message = "Warehouse ID is required")
    Long warehouseId;

    @NotBlank(message = "Supplier ID is required")
    String supplierId;

    @NotBlank(message = "User ID is required")
    @Size(max = 50, message = "User ID must not exceed 50 characters")
    String userId;

    @NotNull(message = "Created date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate createdDate;

    @Size(max = 255, message = "Note must not exceed 255 characters")
    String note;

    @Valid
    @NotEmpty(message = "Import invoice details are required")
    List<CreateImportInvoiceDetailRequest> importInvoiceDetails;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.01", message = "Total amount must be greater than 0")
    @Digits(integer = 13, fraction = 2, message = "Total amount must have at most 13 digits and 2 decimal places")
    BigDecimal totalAmount;

    @DecimalMin(value = "0", message = "Discount value must not be negative")
    @Digits(integer = 13, fraction = 2, message = "Discount value must have at most 13 digits and 2 decimal places")
    BigDecimal discountValue;

    DiscountType discountType;

    @DecimalMin(value = "0", message = "VAT rate must not be negative")
    @DecimalMax(value = "100", message = "VAT rate must not exceed 100")
    @Digits(integer = 2, fraction = 2, message = "VAT rate must have at most 2 digits and 2 decimal places")
    BigDecimal vatRate;

    @NotNull(message = "Final amount is required")
    @DecimalMin(value = "0", message = "Final amount must not be negative")
    @Digits(integer = 13, fraction = 2, message = "Final amount must have at most 13 digits and 2 decimal places")
    BigDecimal finalAmount;
}
