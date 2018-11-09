package com.tiket.poc.testing.rxjava;

import com.tiket.poc.testing.basic.Subscription;
import io.reactivex.Single;

/**
 * @author zakyalvan
 */
public interface SubscriberRepository {
    Single<RegisteredSubscriber> register(Subscription subscription);
}
