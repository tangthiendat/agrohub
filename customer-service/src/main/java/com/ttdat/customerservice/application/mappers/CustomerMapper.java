package com.ttdat.customerservice.application.mappers;

import com.ttdat.customerservice.api.dto.CustomerDTO;
import com.ttdat.customerservice.domain.entities.Customer;
import com.ttdat.customerservice.domain.events.customer.CustomerCreatedEvent;
import com.ttdat.customerservice.domain.events.customer.CustomerUpdatedEvent;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
    Customer toEntity(CustomerCreatedEvent customerCreatedEvent);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromEvent(@MappingTarget Customer customer, CustomerUpdatedEvent customerUpdatedEvent);
}
