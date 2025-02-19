package com.ttdat.purchaseservice.application.mappers;

import com.ttdat.purchaseservice.api.dto.common.SupplierDTO;
import com.ttdat.purchaseservice.domain.entities.Supplier;
import com.ttdat.purchaseservice.domain.events.SupplierCreatedEvent;
import com.ttdat.purchaseservice.domain.events.SupplierUpdatedEvent;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SupplierMapper extends EntityMapper<SupplierDTO, Supplier> {
    Supplier toEntity(SupplierCreatedEvent supplierCreatedEvent);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromEvent(@MappingTarget Supplier supplier, SupplierUpdatedEvent supplierUpdatedEvent);
}
