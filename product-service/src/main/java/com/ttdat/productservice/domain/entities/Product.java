package com.ttdat.productservice.domain.entities;

import com.ttdat.productservice.infrastructure.audit.Auditable;
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
@Table(name = "products")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends Auditable {

    @Id
    @Column(length = 50)
    String productId;

    @Column(length = 200, nullable = false)
    String productName;

    @Column(scale = 2)
    Double totalQuantity;

    @Column(length = 500)
    String description;

    @Column(nullable = false)
    String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    Category category;

    @Column(nullable = false)
    Integer defaultExpDays;

    @Column(length = 200)
    String storageInstructions;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductUnit> productUnits;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    PhysicalState physicalState;

    @Column(length = 50)
    String packaging;

    @Column(length = 300)
    String safetyInstructions;

    @Column(length = 200)
    String hazardClassification;

    @Column(length = 200)
    String ppeRequired;
}
