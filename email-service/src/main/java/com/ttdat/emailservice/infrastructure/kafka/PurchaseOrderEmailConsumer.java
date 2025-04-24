package com.ttdat.emailservice.infrastructure.kafka;

import com.ttdat.core.infrastructure.kafka.email.PurchaseOrderCreatedMessage;
import com.ttdat.emailservice.domain.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PurchaseOrderEmailConsumer {
    private final EmailService emailService;

    @KafkaListener(topics = "${kafka.email.topic}",
            groupId = "purchase-order-group",
            properties = {"spring.json.value.default.type=com.ttdat.core.infrastructure.kafka.email.PurchaseOrderCreatedMessage"})
    public void listenPurchaseOrderEmailNotification(@Payload PurchaseOrderCreatedMessage purchaseOrderCreatedMessage) {
        emailService.sendEmail(purchaseOrderCreatedMessage.getToEmail(),
                purchaseOrderCreatedMessage.getTemplateName(),
                purchaseOrderCreatedMessage.getSubject(),
                purchaseOrderCreatedMessage.getContext());
    }
}
