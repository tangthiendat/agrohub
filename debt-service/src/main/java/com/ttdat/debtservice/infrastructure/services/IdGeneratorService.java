package com.ttdat.debtservice.infrastructure.services;

public interface IdGeneratorService {
    String generateTransactionId();
    String generatePaymentId();
    String generateReceiptId();
}
