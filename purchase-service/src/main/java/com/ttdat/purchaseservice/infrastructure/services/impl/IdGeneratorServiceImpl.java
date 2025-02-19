package com.ttdat.purchaseservice.infrastructure.services.impl;

import com.ttdat.purchaseservice.infrastructure.services.IdGeneratorService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class IdGeneratorServiceImpl implements IdGeneratorService {

    @Override
    public String generateSupplierId() {
        return RandomStringUtils.secure().nextNumeric(10);
    }
}
