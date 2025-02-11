package com.ttdat.productservice.application.handlers.command;

import com.ttdat.productservice.application.commands.category.CreateCategoryCommand;
import com.ttdat.productservice.application.commands.category.UpdateCategoryCommand;
import com.ttdat.productservice.application.mappers.CategoryMapper;
import com.ttdat.productservice.domain.events.category.CategoryCreatedEvent;
import com.ttdat.productservice.domain.events.category.CategoryUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryCommandHandler {
    private final EventBus eventBus;
    private final CategoryMapper categoryMapper;

    @CommandHandler
    public void handle(CreateCategoryCommand createCategoryCommand) {
        CategoryCreatedEvent categoryCreatedEvent = categoryMapper.toCreateEvent(createCategoryCommand);
        eventBus.publish(GenericEventMessage.asEventMessage(categoryCreatedEvent));
    }

    @CommandHandler
    public void handle(UpdateCategoryCommand updateCategoryCommand) {
        CategoryUpdatedEvent categoryUpdatedEvent = categoryMapper.toUpdateEvent(updateCategoryCommand);
        eventBus.publish(GenericEventMessage.asEventMessage(categoryUpdatedEvent));
    }
}
