package com.tiket.poc.testing.basic;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author zakyalvan
 */
public interface SubscriptionHandler {
    void handle(@NotNull @Valid Subscription subscription);
}
