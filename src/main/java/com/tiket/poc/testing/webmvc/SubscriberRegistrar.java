package com.tiket.poc.testing.webmvc;

import io.reactivex.Single;

/**
 * Handle registration of subscriber.
 *
 * @author zakyalvan
 */
public interface SubscriberRegistrar {
    Single<SubscriptionResult> register(SubscriptionRequest request);
}
