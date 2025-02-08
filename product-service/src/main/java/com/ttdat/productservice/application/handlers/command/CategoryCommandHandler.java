package com.ttdat.productservice.application.handlers.command;

import com.ttdat.productservice.application.commands.category.CreateCategoryCommand;
import com.ttdat.productservice.application.mappers.CategoryMapper;
import com.ttdat.productservice.domain.events.category.CategoryCreatedEvent;
import com.ttdat.productservice.infrastructure.services.IdGeneratorService;
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
    private final IdGeneratorService idGeneratorService;

    @CommandHandler
    public void handle(CreateCategoryCommand createCategoryCommand) {
        Long categoryId = idGeneratorService.generateCategoryId();
        CategoryCreatedEvent categoryCreatedEvent = categoryMapper.toEvent(categoryId, createCategoryCommand);
        eventBus.publish(GenericEventMessage.asEventMessage(categoryCreatedEvent));
    }
}
