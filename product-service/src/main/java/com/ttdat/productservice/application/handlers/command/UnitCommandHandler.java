package com.ttdat.productservice.application.handlers.command;

import com.ttdat.productservice.application.commands.unit.CreateUnitCommand;
import com.ttdat.productservice.application.commands.unit.UpdateUnitCommand;
import com.ttdat.productservice.application.mappers.UnitMapper;
import com.ttdat.productservice.domain.events.unit.UnitCreatedEvent;
import com.ttdat.productservice.domain.events.unit.UnitUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnitCommandHandler {
    private final EventBus eventBus;
    private final UnitMapper unitMapper;

    @CommandHandler
    public void handleCreateUnitCommand(CreateUnitCommand createUnitCommand) {
        UnitCreatedEvent unitCreatedEvent = unitMapper.toCreateEvent(createUnitCommand);
        eventBus.publish(GenericEventMessage.asEventMessage(unitCreatedEvent));
    }

    @CommandHandler
    public void handleUpdateUnitCommand(UpdateUnitCommand updateUnitCommand) {
        UnitUpdatedEvent unitUpdatedEvent = unitMapper.toUpdateEvent(updateUnitCommand);
        eventBus.publish(GenericEventMessage.asEventMessage(unitUpdatedEvent));
    }
}
