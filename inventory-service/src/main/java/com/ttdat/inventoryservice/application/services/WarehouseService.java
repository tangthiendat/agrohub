package com.ttdat.inventoryservice.application.services;

import com.ttdat.inventoryservice.api.dto.common.WarehouseDTO;

public interface WarehouseService {

    void createWarehouse(WarehouseDTO warehouseDTO);

    void updateWarehouse(Long warehouseId, WarehouseDTO warehouseDTO);
}
