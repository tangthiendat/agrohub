package com.ttdat.inventoryservice.domain.entities;

import com.ttdat.inventoryservice.infrastructure.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "warehouses")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Warehouse extends Auditable {

    @Id
    Long warehouseId;

    @Column(length = 100, nullable = false)
    String warehouseName;

    String address;

    @OneToMany(mappedBy = "warehouse")
    List<ProductBatch> productBatches;

    @OneToMany(mappedBy = "warehouse")
    List<ProductStock> productStocks;

    @OneToMany(mappedBy = "warehouse")
    List<ProductLocation> productLocations;
}
