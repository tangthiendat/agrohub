package com.ttdat.inventoryservice.application.services;

import com.ttdat.inventoryservice.api.dto.common.ProductLocationDTO;

public interface ProductLocationService {
    void createProductLocation(ProductLocationDTO productLocationDTO);

    void updateProductLocation(String id, ProductLocationDTO productLocationDTO);
}
