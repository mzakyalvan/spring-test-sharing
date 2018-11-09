package com.tiket.poc.testing.redis;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author zakyalvan
 */
public interface SubscriptionService {

    RegisteredSubscriber registerSubscriber(@NotBlank @Email String emailAddress, @NotBlank String fullName);

    RegisteredSubscriber findSubscription(@Email String emailAddress);

    void cancelSubscription(@Email String emailAddress);
}
