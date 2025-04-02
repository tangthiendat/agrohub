package com.ttdat.debtservice.domain.services;

import com.ttdat.core.domain.entities.DebtStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DebtDomainService {

    public DebtStatus getDebtStatus(BigDecimal remainingAmount, BigDecimal totalAmount) {
        if (remainingAmount.compareTo(BigDecimal.ZERO) == 0) {
            return DebtStatus.PAID;
        } else if (remainingAmount.compareTo(BigDecimal.ZERO) > 0 && remainingAmount.compareTo(totalAmount) < 0) {
            return DebtStatus.PARTIALLY_PAID;
        } else {
            return DebtStatus.UNPAID;
        }
    }
}
