package com.ttdat.debtservice.application.repositories;

import com.ttdat.debtservice.domain.entities.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, String> {
}
