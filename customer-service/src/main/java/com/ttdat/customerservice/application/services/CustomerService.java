package com.ttdat.customerservice.application.services;

import com.ttdat.customerservice.api.dto.CustomerDTO;
import com.ttdat.customerservice.api.dto.request.UpdateCustomerStatusRequest;

public interface CustomerService {
    void createCustomer(CustomerDTO customerDTO);
    void updateCustomer(String customerId, CustomerDTO customerDTO);
    void updateStatus(String customerId, UpdateCustomerStatusRequest updateCustomerStatusRequest);
}
