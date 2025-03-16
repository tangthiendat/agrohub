package com.ttdat.inventoryservice.application.services;

import com.ttdat.inventoryservice.api.dto.request.CreateProductLocationRequest;
import com.ttdat.inventoryservice.api.dto.request.UpdateProductLocationRequest;

public interface ProductLocationService {
    void createProductLocation(CreateProductLocationRequest createProductLocationRequest);

    void updateProductLocation(String id, UpdateProductLocationRequest updateProductLocationRequest);
}
