package com.ttdat.purchaseservice.application.services.impl;

import com.ttdat.purchaseservice.api.dto.common.SupplierDTO;
import com.ttdat.purchaseservice.api.dto.common.SupplierProductDTO;
import com.ttdat.purchaseservice.api.dto.request.CreateSupplierRatingRequest;
import com.ttdat.purchaseservice.api.dto.request.UpdateSupplierStatusRequest;
import com.ttdat.purchaseservice.application.commands.supplier.CreateSupplierCommand;
import com.ttdat.purchaseservice.application.commands.supplier.CreateSupplierProductCommand;
import com.ttdat.purchaseservice.application.commands.supplier.CreateSupplierRatingCommand;
import com.ttdat.purchaseservice.application.commands.supplier.UpdateSupplierCommand;
import com.ttdat.purchaseservice.application.services.SupplierService;
import com.ttdat.purchaseservice.infrastructure.services.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    @Override
    public void updateSupplierStatus(String id, UpdateSupplierStatusRequest updateSupplierStatusRequest) {
        UpdateSupplierCommand updateSupplierCommand = UpdateSupplierCommand.builder()
                .supplierId(id)
                .active(updateSupplierStatusRequest.isActive())
                .build();
        commandGateway.sendAndWait(updateSupplierCommand);
    }

    @Override
    public void createSupplierProduct(SupplierProductDTO supplierProductDTO) {
        CreateSupplierProductCommand createSupplierProductCommand = CreateSupplierProductCommand.builder()
                .supplierProductId(idGeneratorService.generateSupplierProductId())
                .supplierId(supplierProductDTO.getSupplier().getSupplierId())
                .productId(supplierProductDTO.getProductId())
                .build();
        commandGateway.sendAndWait(createSupplierProductCommand);
    }


    @Override
    public void createSupplierRating(String supplierId, CreateSupplierRatingRequest createSupplierRatingRequest) {
        CreateSupplierRatingCommand createSupplierRatingCommand = CreateSupplierRatingCommand.builder()
                .ratingId(UUID.randomUUID().toString())
                .warehouseId(createSupplierRatingRequest.getWarehouseId())
                .supplierId(supplierId)
                .trustScore(createSupplierRatingRequest.getTrustScore())
                .comment(createSupplierRatingRequest.getComment())
                .build();
        commandGateway.sendAndWait(createSupplierRatingCommand);
    }
}
