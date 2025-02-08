package com.ttdat.productservice.application.services;

import com.ttdat.productservice.api.dto.common.UnitDTO;

public interface UnitService {
    void createUnit(UnitDTO unitDTO);

    void updateUnit(Long id, UnitDTO unitDTO);
}
