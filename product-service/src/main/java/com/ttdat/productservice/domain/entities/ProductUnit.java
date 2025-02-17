package com.ttdat.productservice.domain.entities;

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
@Table(name = "product_units")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUnit {

    @Id
    @Column(length = 50)
    String productUnitId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    Unit unit;

    Double conversionFactor;

    boolean isDefault;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "productUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductUnitPrice> productUnitPrices;
}
