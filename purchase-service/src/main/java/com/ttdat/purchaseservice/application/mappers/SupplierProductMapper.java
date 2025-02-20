package com.ttdat.purchaseservice.application.mappers;

import com.ttdat.purchaseservice.api.dto.common.SupplierProductDTO;
import com.ttdat.purchaseservice.domain.entities.SupplierProduct;
import com.ttdat.purchaseservice.domain.events.supplier.SupplierProductCreatedEvent;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {SupplierMapper.class})
public interface SupplierProductMapper extends EntityMapper<SupplierProductDTO, SupplierProduct> {
    @Mapping(target = "supplier.supplierId", source = "supplierId")
    SupplierProduct toEntity(SupplierProductCreatedEvent supplierProductCreatedEvent);
}
