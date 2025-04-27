package com.ttdat.purchaseservice.domain.repositories;

import com.ttdat.purchaseservice.domain.entities.SupplierRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRatingRepository extends JpaRepository<SupplierRating, String> {
}
