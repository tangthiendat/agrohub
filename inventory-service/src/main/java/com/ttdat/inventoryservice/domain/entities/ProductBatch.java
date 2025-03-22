package com.ttdat.inventoryservice.domain.entities;

import com.ttdat.inventoryservice.infrastructure.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_batches")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductBatch extends Auditable {
    @Id
    @Column(length = 50)
    String batchId;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    Warehouse warehouse;

    @Column(length = 50, nullable = false)
    String productId;

    @Column(length = 50, nullable = false)
    String importInvoiceDetailId;

    @Column(nullable = false)
    LocalDate manufacturingDate;

    @Column(nullable = false)
    LocalDate expirationDate;

    @Column(nullable = false)
    LocalDate receivedDate;

    @Column(nullable = false)
    Integer quantity;

    @OneToMany(mappedBy = "productBatch",fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<ProductBatchLocation> batchLocations;
}
