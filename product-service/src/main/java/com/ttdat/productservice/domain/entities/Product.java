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

    @Column(length = 200)
    String productName;

    Integer totalQuantity;

    @Column(length = 500)
    String description;

    String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    Integer defaultExpDays;

    @Column(length = 200)
    String storageInstructions;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductUnit> productUnits;

    @Enumerated(EnumType.STRING)
    PhysicalState physicalState;

    @Column(length = 50)
    String packaging;

    @Column(length = 500)
    String safetyInstructions;

    @Column(length = 200)
    String hazardClassification;

    @Column(length = 200)
    String ppeRequired;
}
