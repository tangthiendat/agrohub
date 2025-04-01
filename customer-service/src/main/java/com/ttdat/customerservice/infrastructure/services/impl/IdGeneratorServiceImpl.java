package com.ttdat.customerservice.infrastructure.services.impl;

import com.ttdat.customerservice.infrastructure.services.IdGeneratorService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class IdGeneratorServiceImpl implements IdGeneratorService {

    @Override
    public String generateCustomerId() {
        return RandomStringUtils.secure().nextNumeric(10);
    }
}
