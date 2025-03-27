package com.ttdat.debtservice.infrastructure.services.impl;

import com.ttdat.debtservice.infrastructure.services.IdGeneratorService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class IdGeneratorServiceImpl implements IdGeneratorService {
    @Override
    public String generateTransactionId() {
        return RandomStringUtils.secure().nextNumeric(20);
    }

    @Override
    public String generatePaymentId() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String datePart = currentDate.format(formatter);
        String randomPart = RandomStringUtils.secure().nextAlphanumeric(8).toUpperCase();
        return datePart + randomPart;
    }
}
