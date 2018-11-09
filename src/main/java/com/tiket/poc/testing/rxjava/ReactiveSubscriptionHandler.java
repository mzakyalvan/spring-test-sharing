package com.tiket.poc.testing.rxjava;

import com.tiket.poc.testing.basic.Subscription;
import io.reactivex.Single;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author zakyalvan
 */
public interface ReactiveSubscriptionHandler {
    Single<RegisteredSubscriber> handle(@NotNull @Valid Subscription subscription);
}
