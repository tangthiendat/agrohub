package com.ttdat.purchaseservice.domain.entities;

import com.ttdat.purchaseservice.infrastructure.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "supplier_ratings",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"supplier_id", "warehouse_id"})
        })
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierRating extends Auditable {
    @Id
    @Column(length = 50)
    String ratingId;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    Supplier supplier;

    Long warehouseId;

    Integer trustScore;

    @Column(length = 300)
    String comment;
}
