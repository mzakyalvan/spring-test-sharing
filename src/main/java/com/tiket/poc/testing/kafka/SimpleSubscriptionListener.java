package com.tiket.poc.testing.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author zakyalvan
 */
@Slf4j
@Component
public class SimpleSubscriptionListener implements SubscriptionListener {
    @Override
    @KafkaListener(topics = "subscribe.register")
    public void handleSubscription(@Payload SubscriptionData subscription) {
        log.info("Handle subscription with data {}", subscription);
    }
}
