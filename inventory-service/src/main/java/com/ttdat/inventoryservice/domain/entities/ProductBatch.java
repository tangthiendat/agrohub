package com.ttdat.inventoryservice.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_batches")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductBatch {
    @Id
    @Column(length = 50)
    String batchId;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    Warehouse warehouse;

    @Column(length = 50)
    String productId;

    @Column(length = 50)
    String importInvoiceDetailId;

    LocalDate manufacturingDate;

    LocalDate expirationDate;

    LocalDate receivedDate;

    Integer quantity;
}
