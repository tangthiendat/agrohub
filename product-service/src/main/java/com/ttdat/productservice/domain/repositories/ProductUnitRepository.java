package com.ttdat.productservice.domain.repositories;

import com.ttdat.productservice.domain.entities.ProductUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductUnitRepository extends JpaRepository<ProductUnit, String> {
}
