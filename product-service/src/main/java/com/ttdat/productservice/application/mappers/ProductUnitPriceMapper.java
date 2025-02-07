package com.ttdat.productservice.application.mappers;

import com.ttdat.productservice.api.dto.common.ProductUnitPriceDTO;
import com.ttdat.productservice.domain.entities.ProductUnitPrice;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductUnitPriceMapper extends EntityMapper<ProductUnitPriceDTO, ProductUnitPrice> {
}
