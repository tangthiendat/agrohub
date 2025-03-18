package com.ttdat.inventoryservice.application.mappers;

import com.ttdat.inventoryservice.domain.entities.ProductBatchLocation;
import com.ttdat.inventoryservice.domain.events.batch.EvtProductBatchLocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductBatchLocationMapper {
    @Mapping(target = "productLocation.locationId", source = "locationId")
    ProductBatchLocation toEntity(EvtProductBatchLocation evtProductBatchLocation);

    List<ProductBatchLocation> toEntityList(List<EvtProductBatchLocation> evtProductBatchLocationList);
}
