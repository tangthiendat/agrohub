package com.ttdat.salesservice.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "export_invoice_detail_batches")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExportInvoiceDetailBatch {
    @Id
    String exportInvoiceDetailBatchId;

    @ManyToOne
    @JoinColumn(name = "export_invoice_detail_id", nullable = false)
    ExportInvoiceDetail exportInvoiceDetail;

    @Column(length = 50, nullable = false)
    String batchId;

    @Column(scale = 2, nullable = false)
    Double quantity;
}
