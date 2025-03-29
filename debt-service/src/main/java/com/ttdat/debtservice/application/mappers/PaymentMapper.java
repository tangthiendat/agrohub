package com.ttdat.debtservice.application.mappers;

import com.ttdat.debtservice.domain.entities.Payment;
import com.ttdat.debtservice.domain.events.payment.PaymentCreatedEvent;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {PaymentDetailMapper.class})
public interface PaymentMapper {
    @Mapping(target = "paymentMethod.paymentMethodId", source = "paymentMethodId")
    Payment toEntity(PaymentCreatedEvent paymentCreatedEvent);

    @AfterMapping
    default void setPaymentIdDetails(@MappingTarget Payment payment, PaymentCreatedEvent paymentCreatedEvent) {
        if(payment.getPaymentDetails() != null) {
            payment.getPaymentDetails().forEach(paymentDetail ->
                    paymentDetail.setPayment(Payment.builder().paymentId(payment.getPaymentId()).build()));
        }
    }
}
