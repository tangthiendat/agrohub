package com.ttdat.customerservice.application.handlers.event;

import com.ttdat.customerservice.application.mappers.CustomerMapper;
import com.ttdat.customerservice.application.repositories.CustomerRepository;
import com.ttdat.customerservice.domain.entities.Customer;
import com.ttdat.customerservice.domain.events.CustomerCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@ProcessingGroup("customer-group")
public class CustomerEventHandler {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional
    @EventHandler
    public void on(CustomerCreatedEvent customerCreatedEvent) {
        Customer customer = customerMapper.toEntity(customerCreatedEvent);
        customerRepository.save(customer);
    }
}
