package com.ttdat.customerservice.application.handlers.command;

import com.ttdat.customerservice.application.commands.customer.CreateCustomerCommand;
import com.ttdat.customerservice.application.commands.customer.UpdateCustomerCommand;
import com.ttdat.customerservice.application.commands.customer.UpdateCustomerStatusCommand;
import com.ttdat.customerservice.domain.events.customer.CustomerCreatedEvent;
import com.ttdat.customerservice.domain.events.customer.CustomerStatusUpdatedEvent;
import com.ttdat.customerservice.domain.events.customer.CustomerUpdatedEvent;
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

    @CommandHandler
    public void handle(UpdateCustomerCommand updateCustomerCommand){
        CustomerUpdatedEvent customerUpdatedEvent = CustomerUpdatedEvent.builder()
                .customerId(updateCustomerCommand.getCustomerId())
                .customerName(updateCustomerCommand.getCustomerName())
                .customerType(updateCustomerCommand.getCustomerType())
                .email(updateCustomerCommand.getEmail())
                .phoneNumber(updateCustomerCommand.getPhoneNumber())
                .active(updateCustomerCommand.isActive())
                .address(updateCustomerCommand.getAddress())
                .taxCode(updateCustomerCommand.getTaxCode())
                .notes(updateCustomerCommand.getNotes())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(customerUpdatedEvent));
    }

    @CommandHandler
    public void handle(UpdateCustomerStatusCommand updateCustomerStatusCommand){
        CustomerStatusUpdatedEvent customerStatusUpdatedEvent = CustomerStatusUpdatedEvent.builder()
                .customerId(updateCustomerStatusCommand.getCustomerId())
                .active(updateCustomerStatusCommand.isActive())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(customerStatusUpdatedEvent));
    }
}
