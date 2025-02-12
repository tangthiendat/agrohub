package com.ttdat.productservice.domain.repositories;

import com.ttdat.productservice.domain.entities.ProductUnitPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductUnitPriceRepository extends JpaRepository<ProductUnitPrice, String> {
}
