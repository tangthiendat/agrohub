package com.ttdat.inventoryservice.domain.entities;

import com.ttdat.inventoryservice.infrastructure.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_stock", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"warehouse_id", "product_id"})
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductStock extends Auditable {

    @Id
    @Column(length = 50)
    String productStockId;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    Warehouse warehouse;

    @Column(length = 50, nullable = false)
    String productId;

    @Column(scale = 2, nullable = false)
    Double quantity;
}
