package com.ttdat.debtservice.application.mappers;

import com.ttdat.debtservice.api.dto.common.PaymentMethodDTO;
import com.ttdat.debtservice.domain.entities.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMethodMapper extends EntityMapper<PaymentMethodDTO, PaymentMethod> {

}
