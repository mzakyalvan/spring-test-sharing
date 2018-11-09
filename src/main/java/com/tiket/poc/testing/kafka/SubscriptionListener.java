package com.tiket.poc.testing.kafka;

/**
 * @author zakyalvan
 */
public interface SubscriptionListener {
    void handleSubscription(SubscriptionData update);
}
