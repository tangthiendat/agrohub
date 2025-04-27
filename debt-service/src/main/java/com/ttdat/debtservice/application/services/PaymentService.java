package com.ttdat.debtservice.application.services;

import com.ttdat.debtservice.api.dto.request.CreatePaymentRequest;

public interface PaymentService {
    void createPayment(CreatePaymentRequest createPaymentRequest);
}
