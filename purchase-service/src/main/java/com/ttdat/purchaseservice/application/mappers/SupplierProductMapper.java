package com.ttdat.purchaseservice.application.mappers;

import com.ttdat.purchaseservice.api.dto.common.SupplierProductDTO;
import com.ttdat.purchaseservice.domain.entities.SupplierProduct;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SupplierProductMapper extends EntityMapper<SupplierProductDTO, SupplierProduct> {
}
