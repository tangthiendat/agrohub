package com.ttdat.salesservice.domain.entities;

import com.ttdat.core.domain.entities.DiscountType;
import com.ttdat.salesservice.infrastructure.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "export_invoices")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExportInvoice extends Auditable {
    @Id
    @Column(length = 20)
    String exportInvoiceId;

    Long warehouseId;

    String customerId;

    @Column(length = 50)
    String userId;

    LocalDate createdDate;

    String note;

    @OneToMany(mappedBy = "exportInvoice", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<ExportInvoiceDetail> exportInvoiceDetails;

    @Column(precision = 15, scale = 2)
    BigDecimal totalAmount;

    @Column(precision = 15, scale = 2)
    BigDecimal discountValue;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    DiscountType discountType;

    @Column(precision = 5, scale = 2)
    BigDecimal vatRate;

    @Column(precision = 15, scale = 2)
    BigDecimal finalAmount;
}
