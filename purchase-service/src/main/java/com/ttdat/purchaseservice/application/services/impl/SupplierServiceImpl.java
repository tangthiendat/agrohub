package com.ttdat.purchaseservice.application.services.impl;

import com.ttdat.purchaseservice.api.dto.common.SupplierDTO;
import com.ttdat.purchaseservice.application.commands.supplier.CreateSupplierCommand;
import com.ttdat.purchaseservice.application.commands.supplier.UpdateSupplierCommand;
import com.ttdat.purchaseservice.application.services.SupplierService;
import com.ttdat.purchaseservice.infrastructure.services.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final IdGeneratorService idGeneratorService;
    private final CommandGateway commandGateway;

    @Override
    public void createSupplier(SupplierDTO supplierDTO) {
        CreateSupplierCommand createSupplierCommand = CreateSupplierCommand.builder()
                .supplierId(idGeneratorService.generateSupplierId())
                .supplierName(supplierDTO.getSupplierName())
                .email(supplierDTO.getEmail())
                .phoneNumber(supplierDTO.getPhoneNumber())
                .active(supplierDTO.isActive())
                .address(supplierDTO.getAddress())
                .taxCode(supplierDTO.getTaxCode())
                .contactPerson(supplierDTO.getContactPerson())
                .notes(supplierDTO.getNotes())
                .build();
        commandGateway.sendAndWait(createSupplierCommand);
    }

    @Override
    public void updateSupplier(String id, SupplierDTO supplierDTO) {
        UpdateSupplierCommand updateSupplierCommand = UpdateSupplierCommand.builder()
                .supplierId(id)
                .supplierName(supplierDTO.getSupplierName())
                .email(supplierDTO.getEmail())
                .phoneNumber(supplierDTO.getPhoneNumber())
                .active(supplierDTO.isActive())
                .address(supplierDTO.getAddress())
                .taxCode(supplierDTO.getTaxCode())
                .contactPerson(supplierDTO.getContactPerson())
                .notes(supplierDTO.getNotes())
                .build();
        commandGateway.sendAndWait(updateSupplierCommand);
    }
}
