package com.ttdat.purchaseservice.application.services;

import com.ttdat.purchaseservice.api.dto.common.SupplierDTO;

public interface SupplierService {
    void createSupplier(SupplierDTO supplierDTO);

    void updateSupplier(String id, SupplierDTO supplierDTO);
}
