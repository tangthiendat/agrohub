package com.ttdat.inventoryservice.domain.entities;

import com.ttdat.inventoryservice.infrastructure.audit.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

    String warehouseName;

    String address;

    @OneToMany(mappedBy = "warehouse")
    List<ProductBatch> productBatches;
}
