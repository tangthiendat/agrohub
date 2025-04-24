package com.ttdat.emailservice.infrastructure.kafka;

import com.ttdat.core.infrastructure.kafka.email.PurchaseOrderCreatedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PurchaseOrderEmailConsumer {

    @KafkaListener(topics = "${kafka.email.topic}",
            groupId = "purchase-order-group",
            properties = {"spring.json.value.default.type=com.ttdat.core.infrastructure.kafka.email.PurchaseOrderCreatedMessage"})
    public void listenPurchaseOrderEmailNotification(@Payload PurchaseOrderCreatedMessage purchaseOrderCreatedMessage) {
        log.info("Received purchase order email notification: {}", purchaseOrderCreatedMessage);
    }
}
