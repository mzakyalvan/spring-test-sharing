package com.tiket.poc.testing.mongodb;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author zakyalvan
 */
public interface SubscriptionService {
    RegisteredSubscriber registerSubscriber(@NotBlank @Email String emailAddress, @NotBlank String fullName);
}
