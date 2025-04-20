package com.ttdat.debtservice.domain.repositories;

import com.ttdat.debtservice.domain.entities.DebtTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebtTransactionRepository extends JpaRepository<DebtTransaction, String> {
}
