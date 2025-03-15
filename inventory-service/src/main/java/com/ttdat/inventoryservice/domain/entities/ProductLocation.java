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
@Table(name = "product_locations")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductLocation {
    @Id
    String locationId;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    Warehouse warehouse;

    String rackName;

    @Enumerated(EnumType.STRING)
    RackType rackType;

    Integer rowNumber;

    Integer columnNumber;

    @Enumerated(EnumType.STRING)
    LocationStatus status;
}
