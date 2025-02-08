package com.ttdat.productservice.application.handlers.event;

import com.ttdat.core.application.exceptions.DuplicateResourceException;
import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.productservice.application.mappers.UnitMapper;
import com.ttdat.productservice.domain.entities.Unit;
import com.ttdat.productservice.domain.events.unit.UnitCreatedEvent;
import com.ttdat.productservice.domain.repositories.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnitEventHandler {
    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    @EventHandler
    public void handle(UnitCreatedEvent unitCreatedEvent) {
        if (unitRepository.existsByUnitName(unitCreatedEvent.getUnitName())) {
            throw new DuplicateResourceException(ErrorCode.UNIT_ALREADY_EXISTS);
        }
        Unit unit = unitMapper.toEntity(unitCreatedEvent);
        unitRepository.save(unit);
    }

}
