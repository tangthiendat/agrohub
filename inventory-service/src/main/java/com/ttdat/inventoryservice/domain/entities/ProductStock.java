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
@Table(name = "product_stock", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"warehouse_id", "product_id"})
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductStock {

    @Id
    @Column(length = 50)
    String productStockId;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    Warehouse warehouse;

    @Column(length = 50)
    String productId;

    @Column(scale = 2)
    Double quantity;
}
