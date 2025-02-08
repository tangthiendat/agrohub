package com.ttdat.productservice.application.mappers;

import com.ttdat.productservice.api.dto.common.CategoryDTO;
import com.ttdat.productservice.application.commands.category.CreateCategoryCommand;
import com.ttdat.productservice.application.commands.category.UpdateCategoryCommand;
import com.ttdat.productservice.domain.entities.Category;
import com.ttdat.productservice.domain.events.category.CategoryCreatedEvent;
import com.ttdat.productservice.domain.events.category.CategoryUpdatedEvent;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {
    CreateCategoryCommand toCommand(CategoryDTO categoryDTO);

    @Mapping(source = "id", target = "categoryId")
    CategoryCreatedEvent toEvent(Long id, CreateCategoryCommand createCategoryCommand);

    Category toEntity(CategoryCreatedEvent categoryCreatedEvent);

    @Mapping(source = "id", target = "categoryId")
    UpdateCategoryCommand toCommand(Long id, CategoryDTO categoryDTO);

    CategoryUpdatedEvent toEvent(UpdateCategoryCommand updateCategoryCommand);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromEvent(@MappingTarget Category category, CategoryUpdatedEvent categoryUpdatedEvent);
}
