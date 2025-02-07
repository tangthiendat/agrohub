package com.ttdat.productservice.application.mappers;

import com.ttdat.productservice.api.dto.common.UnitDTO;
import com.ttdat.productservice.domain.entities.Unit;
import com.ttdat.productservice.domain.events.unit.UnitCreatedEvent;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UnitMapper extends EntityMapper<UnitDTO, Unit> {
    Unit toEntity(UnitCreatedEvent unitCreatedEvent);
}
