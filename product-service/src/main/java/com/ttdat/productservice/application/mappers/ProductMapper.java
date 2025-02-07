package com.ttdat.productservice.application.mappers;

import com.ttdat.productservice.api.dto.common.ProductDTO;
import com.ttdat.productservice.domain.entities.Product;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {CategoryMapper.class, ProductUnitMapper.class})
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
}
