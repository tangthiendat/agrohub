package com.ttdat.customerservice.application.services;

import com.ttdat.customerservice.api.dto.CustomerDTO;

public interface CustomerService {
    void createCustomer(CustomerDTO customerDTO);
    void updateCustomer(String customerId, CustomerDTO customerDTO);
}
