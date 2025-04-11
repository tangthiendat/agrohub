package com.ttdat.salesservice.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "export_invoice_details")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExportInvoiceDetail {
    @Id
    @Column(length = 50)
    String exportInvoiceDetailId;

    @ManyToOne
    @JoinColumn(name = "export_invoice_id", nullable = false)
    ExportInvoice exportInvoice;

    @Column(length = 50, nullable = false)
    String productId;

    @Column(length = 50, nullable = false)
    String productUnitId;

    @Column(nullable = false)
    Integer quantity;

    @Column(precision = 15, scale = 2, nullable = false)
    BigDecimal unitPrice;

    @OneToMany(mappedBy = "exportInvoiceDetail", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<ExportInvoiceDetailBatch> detailBatches;
}
