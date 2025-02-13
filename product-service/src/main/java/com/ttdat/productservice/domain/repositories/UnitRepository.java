package com.ttdat.productservice.domain.repositories;

import com.ttdat.productservice.domain.entities.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    boolean existsByUnitName(String unitName);
}
