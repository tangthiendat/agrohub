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
@Table(name = "product_locations")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductLocation extends Auditable {
    @Id
    @Column(length = 50)
    String locationId;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    Warehouse warehouse;

    @Column(length = 10)
    String rackName;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    RackType rackType;

    Integer rowNumber;

    Integer columnNumber;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    LocationStatus status;

    String reason;
}
