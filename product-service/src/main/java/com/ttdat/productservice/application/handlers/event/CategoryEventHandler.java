package com.ttdat.productservice.application.handlers.event;

import com.ttdat.core.application.exceptions.DuplicateResourceException;
import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.productservice.application.mappers.CategoryMapper;
import com.ttdat.productservice.domain.entities.Category;
import com.ttdat.productservice.domain.events.category.CategoryCreatedEvent;
import com.ttdat.productservice.domain.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ProcessingGroup("category-group")
@RequiredArgsConstructor
public class CategoryEventHandler {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    @EventHandler
    public void handle(CategoryCreatedEvent categoryCreatedEvent){
        if(categoryRepository.existsByCategoryName(categoryCreatedEvent.getCategoryName())){
            throw new DuplicateResourceException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }
        Category category = categoryMapper.toEntity(categoryCreatedEvent);
        categoryRepository.save(category);
    }
}
