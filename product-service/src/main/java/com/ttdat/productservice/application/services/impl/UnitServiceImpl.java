package com.ttdat.productservice.application.services.impl;

import com.ttdat.productservice.api.dto.common.UnitDTO;
import com.ttdat.productservice.application.commands.unit.CreateUnitCommand;
import com.ttdat.productservice.application.services.UnitService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {
    private final CommandGateway commandGateway;

    @Override
    public void createUnit(UnitDTO unitDTO) {
        CreateUnitCommand createUnitCommand = CreateUnitCommand.builder()
                .unitName(unitDTO.getUnitName())
                .build();
        commandGateway.send(createUnitCommand);
    }
}
