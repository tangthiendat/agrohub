package com.ttdat.purchaseservice.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "supplier_product_quotations")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierProductQuotation {

    @Id
    @Column(length = 50)
    String quotationId;

    @ManyToOne
    @JoinColumn(name = "supplier_product_id")
    SupplierProduct supplierProduct;

    Long unitId;

    @Column(precision = 15, scale = 2)
    BigDecimal unitPrice;

    LocalDateTime validFrom;

    LocalDateTime validTo;
}
