package com.ttdat.debtservice.application.repositories;

import com.ttdat.debtservice.domain.entities.DebtAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DebtAccountRepository extends JpaRepository<DebtAccount, String> {
    @Query("SELECT da FROM DebtAccount da WHERE da.partyId = :partyId " +
            "AND da.remainingAmount > 0")
    List<DebtAccount> getPartyDebtAccounts(String partyId);
}
