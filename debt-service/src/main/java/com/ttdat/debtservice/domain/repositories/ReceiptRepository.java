package com.ttdat.debtservice.domain.repositories;

import com.ttdat.debtservice.domain.entities.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, String> {
}
