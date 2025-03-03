package com.ttdat.productservice.application.handlers.event;

import com.ttdat.core.application.exceptions.DuplicateResourceException;
import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.productservice.application.mappers.CategoryMapper;
import com.ttdat.productservice.domain.entities.Category;
import com.ttdat.productservice.domain.events.category.CategoryCreatedEvent;
import com.ttdat.productservice.domain.events.category.CategoryUpdatedEvent;
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
    public void on(CategoryCreatedEvent categoryCreatedEvent){
        if(categoryRepository.existsByCategoryName(categoryCreatedEvent.getCategoryName())){
            throw new DuplicateResourceException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }
        Category category = categoryMapper.toEntity(categoryCreatedEvent);
        categoryRepository.save(category);
    }

    private Category getCategoryById(Long categoryId){
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Transactional
    @EventHandler
    public void on(CategoryUpdatedEvent categoryUpdatedEvent) {
        Category category = getCategoryById(categoryUpdatedEvent.getCategoryId());
        categoryMapper.updateEntityFromEvent(category, categoryUpdatedEvent);
        categoryRepository.save(category);
    }
}
