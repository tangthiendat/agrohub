package com.ttdat.debtservice.domain.repositories;

import com.ttdat.debtservice.domain.entities.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, String> {
}
