package com.ttdat.purchaseservice.application.handlers.event;

import com.ttdat.core.application.exceptions.DuplicateResourceException;
import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.purchaseservice.application.mappers.SupplierMapper;
import com.ttdat.purchaseservice.domain.entities.Supplier;
import com.ttdat.purchaseservice.domain.events.SupplierCreatedEvent;
import com.ttdat.purchaseservice.domain.events.SupplierStatusUpdatedEvent;
import com.ttdat.purchaseservice.domain.events.SupplierUpdatedEvent;
import com.ttdat.purchaseservice.domain.repositories.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@ProcessingGroup("supplier-group")
public class SupplierEventHandler {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Transactional
    @EventHandler
    public void handle(SupplierCreatedEvent supplierCreatedEvent) {
        if(supplierRepository.existsBySupplierName(supplierCreatedEvent.getSupplierName())) {
            throw new DuplicateResourceException(ErrorCode.SUPPLIER_ALREADY_EXISTS);
        }
        Supplier supplier = supplierMapper.toEntity(supplierCreatedEvent);
        supplierRepository.save(supplier);
    }

    private Supplier getSupplierById(String supplierId) {
        return supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.SUPPLIER_NOT_FOUND));
    }

    @Transactional
    @EventHandler
    public void handle(SupplierUpdatedEvent supplierUpdatedEvent) {
        Supplier supplier = getSupplierById(supplierUpdatedEvent.getSupplierId());
        supplierMapper.updateEntityFromEvent(supplier, supplierUpdatedEvent);
        supplierRepository.save(supplier);
    }

    @Transactional
    @EventHandler
    public void handle(SupplierStatusUpdatedEvent supplierStatusUpdatedEvent) {
        Supplier supplier = getSupplierById(supplierStatusUpdatedEvent.getSupplierId());
        supplier.setActive(supplierStatusUpdatedEvent.isActive());
        supplierRepository.save(supplier);
    }
}
