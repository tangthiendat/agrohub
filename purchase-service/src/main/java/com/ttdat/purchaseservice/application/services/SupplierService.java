package com.ttdat.purchaseservice.application.services;

import com.ttdat.purchaseservice.api.dto.common.SupplierDTO;
import com.ttdat.purchaseservice.api.dto.request.UpdateSupplierStatusRequest;
import jakarta.validation.Valid;

public interface SupplierService {
    void createSupplier(SupplierDTO supplierDTO);

    void updateSupplier(String id, SupplierDTO supplierDTO);

    void updateSupplierStatus(String id, @Valid UpdateSupplierStatusRequest updateSupplierStatusRequest);
}
