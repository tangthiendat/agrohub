package com.ttdat.purchaseservice.application.handlers.event;

import com.ttdat.core.application.exceptions.DuplicateResourceException;
import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.purchaseservice.application.mappers.SupplierMapper;
import com.ttdat.purchaseservice.application.mappers.SupplierProductMapper;
import com.ttdat.purchaseservice.domain.entities.Supplier;
import com.ttdat.purchaseservice.domain.entities.SupplierProduct;
import com.ttdat.purchaseservice.domain.events.supplier.SupplierCreatedEvent;
import com.ttdat.purchaseservice.domain.events.supplier.SupplierProductCreatedEvent;
import com.ttdat.purchaseservice.domain.events.supplier.SupplierStatusUpdatedEvent;
import com.ttdat.purchaseservice.domain.events.supplier.SupplierUpdatedEvent;
import com.ttdat.purchaseservice.domain.repositories.SupplierProductRepository;
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
    private final SupplierProductRepository supplierProductRepository;
    private final SupplierMapper supplierMapper;
    private final SupplierProductMapper supplierProductMapper;

    @Transactional
    @EventHandler
    public void on(SupplierCreatedEvent supplierCreatedEvent) {
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
    public void on(SupplierUpdatedEvent supplierUpdatedEvent) {
        Supplier supplier = getSupplierById(supplierUpdatedEvent.getSupplierId());
        supplierMapper.updateEntityFromEvent(supplier, supplierUpdatedEvent);
        supplierRepository.save(supplier);
    }

    @Transactional
    @EventHandler
    public void on(SupplierStatusUpdatedEvent supplierStatusUpdatedEvent) {
        Supplier supplier = getSupplierById(supplierStatusUpdatedEvent.getSupplierId());
        supplier.setActive(supplierStatusUpdatedEvent.isActive());
        supplierRepository.save(supplier);
    }

    @Transactional
    @EventHandler
    public void on(SupplierProductCreatedEvent supplierProductCreatedEvent) {
        SupplierProduct supplierProduct = supplierProductMapper.toEntity(supplierProductCreatedEvent);
        supplierProductRepository.save(supplierProduct);
    }
}
