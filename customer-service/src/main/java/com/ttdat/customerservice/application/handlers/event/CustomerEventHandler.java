package com.ttdat.customerservice.application.handlers.event;

import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.customerservice.application.mappers.CustomerMapper;
import com.ttdat.customerservice.application.repositories.CustomerRepository;
import com.ttdat.customerservice.domain.entities.Customer;
import com.ttdat.customerservice.domain.events.customer.CustomerCreatedEvent;
import com.ttdat.customerservice.domain.events.customer.CustomerStatusUpdatedEvent;
import com.ttdat.customerservice.domain.events.customer.CustomerUpdatedEvent;
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

    private Customer getCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));
    }

    @Transactional
    @EventHandler
    public void on(CustomerUpdatedEvent customerUpdatedEvent) {
        Customer customer = getCustomerById(customerUpdatedEvent.getCustomerId());
        customerMapper.updateEntityFromEvent(customer, customerUpdatedEvent);
        customerRepository.save(customer);
    }

    @Transactional
    @EventHandler
    public void on(CustomerStatusUpdatedEvent customerStatusUpdatedEvent) {
        Customer customer = getCustomerById(customerStatusUpdatedEvent.getCustomerId());
        customer.setActive(customerStatusUpdatedEvent.isActive());
        customerRepository.save(customer);
    }

}
