package com.ttdat.productservice.application.services;

import com.ttdat.productservice.api.dto.common.UnitDTO;
import jakarta.validation.Valid;

public interface UnitService {
    void createUnit(UnitDTO unitDTO);

    void updateUnit(Long id, @Valid UnitDTO unitDTO);
}
