package com.ttdat.inventoryservice.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_batch_locations")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductBatchLocation {

    @Id
    @Column(length = 50)
    String batchLocationId;

    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    ProductBatch productBatch;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    ProductLocation productLocation;

    @Column(scale = 2, nullable = false)
    Double quantity;
}
