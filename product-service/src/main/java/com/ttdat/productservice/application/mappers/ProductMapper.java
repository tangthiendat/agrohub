package com.ttdat.productservice.application.mappers;

import com.ttdat.core.api.dto.response.ProductInfo;
import com.ttdat.productservice.api.dto.common.ProductDTO;
import com.ttdat.productservice.domain.entities.Category;
import com.ttdat.productservice.domain.entities.Product;
import com.ttdat.productservice.domain.events.product.ProductCreatedEvent;
import com.ttdat.productservice.domain.events.product.ProductUpdatedEvent;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {CategoryMapper.class, ProductUnitMapper.class})
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {

    @Mapping(target = "category.categoryId", source = "categoryId")
    Product toEntity(ProductCreatedEvent productCreatedEvent);

    @Mapping(target = "category.categoryId", source = "categoryId")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromEvent(@MappingTarget Product product, ProductUpdatedEvent productUpdatedEvent);

    ProductInfo toProductInfo(Product product);

    List<ProductInfo> toProductInfoList(List<Product> products);

    @AfterMapping
    default void setProductIdForProductUnits(@MappingTarget Product product, ProductCreatedEvent productCreatedEvent) {
        if(product.getProductUnits() != null) {
            product.getProductUnits().forEach(productUnit ->
                    productUnit.setProduct(Product.builder()
                            .productId(product.getProductId())
                            .build()));
        }
    }

    @AfterMapping
    default void setProductIdForProductUnits(@MappingTarget Product product, ProductUpdatedEvent productUpdatedEvent) {
        if(product.getProductUnits() != null) {
            product.getProductUnits().forEach(productUnit ->
                    productUnit.setProduct(Product.builder()
                            .productId(product.getProductId())
                            .build()));
        }
    }

    @BeforeMapping
    default void updateCategory(@MappingTarget Product product, ProductUpdatedEvent productUpdatedEvent) {
        if(productUpdatedEvent.getCategoryId() != null) {
            if(product.getCategory() == null || !product.getCategory().getCategoryId().equals(productUpdatedEvent.getCategoryId())) {
                product.setCategory(Category.builder().categoryId(productUpdatedEvent.getCategoryId()).build());
            }
        }
    }

}
