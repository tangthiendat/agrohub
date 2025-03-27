package com.ttdat.debtservice.application.handlers.event;

import com.ttdat.debtservice.application.mappers.PaymentMapper;
import com.ttdat.debtservice.application.repositories.PaymentRepository;
import com.ttdat.debtservice.domain.entities.Payment;
import com.ttdat.debtservice.domain.events.payment.PaymentCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@ProcessingGroup("payment-group")
public class PaymentEventHandler {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Transactional
    @EventHandler
    public void on(PaymentCreatedEvent paymentCreatedEvent) {
        Payment payment = paymentMapper.toEntity(paymentCreatedEvent);
        paymentRepository.save(payment);
    }
}
