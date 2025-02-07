package com.ttdat.productservice.application.mappers;

import com.ttdat.productservice.api.dto.common.ProductUnitDTO;
import com.ttdat.productservice.domain.entities.ProductUnit;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {UnitMapper.class, ProductUnitPriceMapper.class})
public interface ProductUnitMapper extends EntityMapper<ProductUnitDTO, ProductUnit> {
}
