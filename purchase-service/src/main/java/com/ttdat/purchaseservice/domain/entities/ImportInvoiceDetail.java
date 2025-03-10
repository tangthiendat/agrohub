package com.ttdat.purchaseservice.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "import_invoice_details")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImportInvoiceDetail {
    @Id
    @Column(length = 50)
    String importInvoiceDetailId;

    @ManyToOne
    @JoinColumn(name = "import_invoice_id")
    ImportInvoice importInvoice;

    @Column(length = 100)
    String productId;

    @Column(length = 50)
    String productUnitId;

    Integer quantity;

    @Column(precision = 15, scale = 2)
    BigDecimal unitPrice;
}
