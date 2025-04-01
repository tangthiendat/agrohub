package com.ttdat.customerservice.application.services.impl;

import com.ttdat.customerservice.api.dto.CustomerDTO;
import com.ttdat.customerservice.application.commands.customer.CreateCustomerCommand;
import com.ttdat.customerservice.application.commands.customer.UpdateCustomerCommand;
import com.ttdat.customerservice.application.services.CustomerService;
import com.ttdat.customerservice.infrastructure.services.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CommandGateway commandGateway;
    private final IdGeneratorService idGeneratorService;

    @Override
    public void createCustomer(CustomerDTO customerDTO) {
        CreateCustomerCommand createCustomerCommand = CreateCustomerCommand.builder()
                .customerId(idGeneratorService.generateCustomerId())
                .customerName(customerDTO.getCustomerName())
                .customerType(customerDTO.getCustomerType())
                .email(customerDTO.getEmail())
                .phoneNumber(customerDTO.getPhoneNumber())
                .active(customerDTO.isActive())
                .address(customerDTO.getAddress())
                .taxCode(customerDTO.getTaxCode())
                .notes(customerDTO.getNotes())
                .build();
        commandGateway.sendAndWait(createCustomerCommand);
    }

    @Override
    public void updateCustomer(String customerId, CustomerDTO customerDTO) {
        UpdateCustomerCommand updateCustomerCommand = UpdateCustomerCommand.builder()
                .customerId(customerId)
                .customerName(customerDTO.getCustomerName())
                .customerType(customerDTO.getCustomerType())
                .email(customerDTO.getEmail())
                .phoneNumber(customerDTO.getPhoneNumber())
                .active(customerDTO.isActive())
                .address(customerDTO.getAddress())
                .taxCode(customerDTO.getTaxCode())
                .notes(customerDTO.getNotes())
                .build();
        commandGateway.sendAndWait(updateCustomerCommand);
    }
}
