package com.ttdat.debtservice.application.services;

import com.ttdat.debtservice.api.dto.request.CreateReceiptRequest;

public interface ReceiptService {
    void createReceipt(CreateReceiptRequest createReceiptRequest);
}
