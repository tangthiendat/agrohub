package com.ttdat.productservice.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_unit_prices")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUnitPrice {

    @Id
    @Column(length = 50)
    String productUnitPriceId;

    @ManyToOne
    @JoinColumn(name = "product_unit_id")
    ProductUnit productUnit;

    @Column(precision = 15, scale = 2)
    BigDecimal price;

    LocalDate startDate;

    LocalDate endDate;
}
