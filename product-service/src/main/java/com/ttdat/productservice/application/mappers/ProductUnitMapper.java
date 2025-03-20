package com.ttdat.productservice.application.mappers;

import com.ttdat.core.api.dto.response.ProductUnitInfo;
import com.ttdat.productservice.api.dto.common.ProductUnitDTO;
import com.ttdat.productservice.domain.entities.ProductUnit;
import com.ttdat.productservice.domain.valueobject.EvtProductUnit;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {UnitMapper.class, ProductUnitPriceMapper.class})
public interface ProductUnitMapper extends EntityMapper<ProductUnitDTO, ProductUnit> {

    @Override
    @Mapping(target = "isDefault", source = "default")
    ProductUnitDTO toDTO(ProductUnit entity);

    @Mapping(target = "unit.unitId", source = "unitId")
    @Mapping(target = "isDefault", source = "default")
    ProductUnit toEntity(EvtProductUnit evtProductUnit);

    ProductUnitInfo toProductUnitInfo(ProductUnit productUnit);

    List<ProductUnitInfo> toProductUnitInfoList(List<ProductUnit> productUnits);

    List<ProductUnit> toEntityList(List<EvtProductUnit> evtProductUnits);

    @AfterMapping
    default void setProductUnitIdForProductUnitPrices(@MappingTarget ProductUnit productUnit, EvtProductUnit evtProductUnit) {
        if (!productUnit.getProductUnitPrices().isEmpty()) {
            productUnit.getProductUnitPrices().forEach(productUnitPrice ->
                    productUnitPrice.setProductUnit(ProductUnit.builder()
                            .productUnitId(productUnit.getProductUnitId())
                            .build()));
        }

    }
}
