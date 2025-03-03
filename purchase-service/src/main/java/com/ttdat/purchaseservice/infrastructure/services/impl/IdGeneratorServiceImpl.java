package com.ttdat.purchaseservice.infrastructure.services.impl;

import com.ttdat.purchaseservice.infrastructure.services.IdGeneratorService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class IdGeneratorServiceImpl implements IdGeneratorService {

    @Override
    public String generateSupplierId() {
        return RandomStringUtils.secure().nextNumeric(10);
    }

    @Override
    public String generateSupplierProductId() {
        return RandomStringUtils.secure().nextAlphanumeric(10);
    }

    @Override
    public String generatePurchaseOrderId() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String datePart = currentDate.format(formatter);
        String randomPart = RandomStringUtils.secure().nextAlphanumeric(8).toUpperCase();
        return datePart + randomPart;
    }
}
