package com.ttdat.debtservice.application.mappers;

import com.ttdat.debtservice.domain.entities.PaymentDetail;
import com.ttdat.debtservice.domain.events.payment.EvtPaymentDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentDetailMapper {
    @Mapping(target = "debtAccount.debtAccountId", source = "debtAccountId")
    PaymentDetail toEntity(EvtPaymentDetail evtPaymentDetail);

    List<PaymentDetail> toEntityList(List<EvtPaymentDetail> paymentDetails);
}
