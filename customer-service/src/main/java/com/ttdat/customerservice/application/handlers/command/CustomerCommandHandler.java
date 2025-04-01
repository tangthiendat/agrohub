package com.ttdat.customerservice.application.handlers.command;

import com.ttdat.customerservice.application.commands.customer.CreateCustomerCommand;
import com.ttdat.customerservice.domain.events.CustomerCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerCommandHandler {
    private final EventBus eventBus;

    @CommandHandler
    public void handle(CreateCustomerCommand createCustomerCommand){
        CustomerCreatedEvent customerCreatedEvent = CustomerCreatedEvent.builder()
                .customerId(createCustomerCommand.getCustomerId())
                .customerName(createCustomerCommand.getCustomerName())
                .customerType(createCustomerCommand.getCustomerType())
                .email(createCustomerCommand.getEmail())
                .phoneNumber(createCustomerCommand.getPhoneNumber())
                .active(createCustomerCommand.isActive())
                .address(createCustomerCommand.getAddress())
                .taxCode(createCustomerCommand.getTaxCode())
                .notes(createCustomerCommand.getNotes())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(customerCreatedEvent));
    }
}
