package com.ttdat.purchaseservice.application.services;

import com.ttdat.purchaseservice.api.dto.common.SupplierDTO;
import com.ttdat.purchaseservice.api.dto.common.SupplierProductDTO;
import com.ttdat.purchaseservice.api.dto.request.CreateSupplierRatingRequest;
import com.ttdat.purchaseservice.api.dto.request.UpdateSupplierStatusRequest;
import jakarta.validation.Valid;

public interface SupplierService {
    void createSupplier(SupplierDTO supplierDTO);

    void updateSupplier(String id, SupplierDTO supplierDTO);

    void updateSupplierStatus(String id, @Valid UpdateSupplierStatusRequest updateSupplierStatusRequest);

    void createSupplierProduct(SupplierProductDTO supplierProductDTO);

    void createSupplierRating(String supplierId, CreateSupplierRatingRequest createSupplierRatingRequest);
}
