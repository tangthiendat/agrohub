package com.ttdat.productservice.application.mappers;

import com.ttdat.productservice.api.dto.common.ProductUnitPriceDTO;
import com.ttdat.productservice.domain.entities.ProductUnitPrice;
import com.ttdat.productservice.domain.valueobject.EvtProductUnitPrice;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductUnitPriceMapper extends EntityMapper<ProductUnitPriceDTO, ProductUnitPrice> {
    ProductUnitPrice toEntity(EvtProductUnitPrice evtProductUnitPrice);

    List<ProductUnitPrice> toEntityList(List<EvtProductUnitPrice> evtProductUnitPrices);
}
