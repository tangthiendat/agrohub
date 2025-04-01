package com.ttdat.customerservice.application.mappers;

import com.ttdat.customerservice.domain.entities.Customer;
import com.ttdat.customerservice.domain.events.CustomerCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {
    Customer toEntity(CustomerCreatedEvent customerCreatedEvent);
}
