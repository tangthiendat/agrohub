package com.ttdat.productservice.application.mappers;

import com.ttdat.productservice.api.dto.common.UnitDTO;
import com.ttdat.productservice.domain.entities.Unit;
import com.ttdat.productservice.domain.events.unit.UnitCreatedEvent;
import com.ttdat.productservice.domain.events.unit.UnitUpdatedEvent;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UnitMapper extends EntityMapper<UnitDTO, Unit> {
    Unit toEntity(UnitCreatedEvent unitCreatedEvent);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromEvent(@MappingTarget Unit unit, UnitUpdatedEvent unitUpdatedEvent);
}
