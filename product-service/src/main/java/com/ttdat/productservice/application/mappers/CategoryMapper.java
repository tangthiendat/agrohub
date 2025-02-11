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
    @Mapping(source = "id", target = "categoryId")
    CreateCategoryCommand toCreateCommand(Long id, CategoryDTO categoryDTO);

    @Mapping(source = "id", target = "categoryId")
    UpdateCategoryCommand toUpdateCommand(Long id, CategoryDTO categoryDTO);

    CategoryCreatedEvent toCreateEvent(CreateCategoryCommand createCategoryCommand);

    CategoryUpdatedEvent toUpdateEvent(UpdateCategoryCommand updateCategoryCommand);

    Category toEntity(CategoryCreatedEvent categoryCreatedEvent);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromEvent(@MappingTarget Category category, CategoryUpdatedEvent categoryUpdatedEvent);
}
