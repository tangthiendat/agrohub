package com.ttdat.purchaseservice.domain.entities;

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
@Table(name = "supplier_products",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"supplier_id", "product_id"})
        })
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierProduct {

    @Id
    @Column(length = 50)
    String supplierProductId;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    Supplier supplier;

    @Column(length = 50)
    String productId;

    @OneToMany(mappedBy = "supplierProduct", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<SupplierProductQuotation> quotations;
}
