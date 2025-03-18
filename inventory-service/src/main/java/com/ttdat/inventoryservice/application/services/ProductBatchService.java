package com.ttdat.inventoryservice.application.services;

import com.ttdat.inventoryservice.api.dto.common.ProductBatchDTO;

public interface ProductBatchService {
    void updateProductBatchLocation(String id, ProductBatchDTO productBatchDTO);
}
