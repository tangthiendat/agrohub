package com.ttdat.inventoryservice.domain.entities;

import com.ttdat.inventoryservice.infrastructure.audit.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Warehouse name is required")
    @Size(max = 100, message = "Warehouse name must not exceed 100 characters")
    String warehouseName;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    String address;

    @OneToMany(mappedBy = "warehouse")
    List<ProductBatch> productBatches;

    @OneToMany(mappedBy = "warehouse")
    List<ProductStock> productStocks;

    @OneToMany(mappedBy = "warehouse")
    List<ProductLocation> productLocations;
}
