package com.ttdat.debtservice.infrastructure.services.impl;

import com.ttdat.debtservice.infrastructure.services.IdGeneratorService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class IdGeneratorServiceImpl implements IdGeneratorService {
    @Override
    public String generateTransactionId() {
        return RandomStringUtils.secure().nextNumeric(20);
    }
}
