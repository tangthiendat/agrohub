package com.ttdat.productservice.application.handlers.command;

import com.ttdat.productservice.application.commands.unit.CreateUnitCommand;
import com.ttdat.productservice.application.commands.unit.UpdateUnitCommand;
import com.ttdat.productservice.domain.events.unit.UnitCreatedEvent;
import com.ttdat.productservice.domain.events.unit.UnitUpdatedEvent;
import com.ttdat.productservice.infrastructure.services.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnitCommandHandler {
    private final EventBus eventBus;
    private final IdGeneratorService idGeneratorService;

    @CommandHandler
    public void handleCreateUnitCommand(CreateUnitCommand createUnitCommand) {
        UnitCreatedEvent unitCreatedEvent = UnitCreatedEvent.builder()
                .unitId(idGeneratorService.generateUnitId())
                .unitName(createUnitCommand.getUnitName())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(unitCreatedEvent));
    }

    @CommandHandler
    public void handleUpdateUnitCommand(UpdateUnitCommand updateUnitCommand) {
        UnitUpdatedEvent unitUpdatedEvent = UnitUpdatedEvent.builder()
                .unitId(updateUnitCommand.getUnitId())
                .unitName(updateUnitCommand.getUnitName())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(unitUpdatedEvent));
    }
}
