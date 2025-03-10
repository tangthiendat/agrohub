package com.ttdat.debtservice.application.repositories;

import com.ttdat.debtservice.domain.entities.DebtAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebtAccountRepository extends JpaRepository<DebtAccount, String> {
}
