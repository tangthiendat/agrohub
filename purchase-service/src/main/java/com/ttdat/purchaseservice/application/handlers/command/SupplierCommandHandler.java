package com.ttdat.purchaseservice.application.handlers.command;

import com.ttdat.purchaseservice.application.commands.supplier.CreateSupplierCommand;
import com.ttdat.purchaseservice.application.commands.supplier.UpdateSupplierCommand;
import com.ttdat.purchaseservice.domain.events.SupplierCreatedEvent;
import com.ttdat.purchaseservice.domain.events.SupplierUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SupplierCommandHandler {
    private final EventBus eventBus;

    @CommandHandler
    public void handle(CreateSupplierCommand createSupplierCommand) {
        SupplierCreatedEvent supplierCreatedEvent = SupplierCreatedEvent.builder()
                .supplierId(createSupplierCommand.getSupplierId())
                .supplierName(createSupplierCommand.getSupplierName())
                .email(createSupplierCommand.getEmail())
                .phoneNumber(createSupplierCommand.getPhoneNumber())
                .active(createSupplierCommand.isActive())
                .address(createSupplierCommand.getAddress())
                .taxCode(createSupplierCommand.getTaxCode())
                .contactPerson(createSupplierCommand.getContactPerson())
                .notes(createSupplierCommand.getNotes())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(supplierCreatedEvent));
    }

    @CommandHandler
    public void handle(UpdateSupplierCommand updateSupplierCommand) {
        SupplierUpdatedEvent supplierUpdatedEvent = SupplierUpdatedEvent.builder()
                .supplierId(updateSupplierCommand.getSupplierId())
                .supplierName(updateSupplierCommand.getSupplierName())
                .email(updateSupplierCommand.getEmail())
                .phoneNumber(updateSupplierCommand.getPhoneNumber())
                .active(updateSupplierCommand.isActive())
                .address(updateSupplierCommand.getAddress())
                .taxCode(updateSupplierCommand.getTaxCode())
                .contactPerson(updateSupplierCommand.getContactPerson())
                .notes(updateSupplierCommand.getNotes())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(supplierUpdatedEvent));
    }

}
