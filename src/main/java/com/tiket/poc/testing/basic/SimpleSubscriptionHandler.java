package com.tiket.poc.testing.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * @author zakyalvan
 */
@Component
@Validated
public class SimpleSubscriptionHandler implements SubscriptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSubscriptionHandler.class);

    @Override
    public void handle(Subscription subscription) {
        LOGGER.info("Subscription handled...");
    }
}
