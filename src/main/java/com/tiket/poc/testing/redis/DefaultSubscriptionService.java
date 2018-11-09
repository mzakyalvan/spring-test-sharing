package com.tiket.poc.testing.redis;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @author zakyalvan
 */
@Service
@Validated
class DefaultSubscriptionService implements SubscriptionService {

    private static final String SUBSCRIPTION_CACHE = "registered-subscriber";

    @Override
    @CachePut(cacheNames = SUBSCRIPTION_CACHE, key = "#emailAddress")
    public RegisteredSubscriber registerSubscriber(String emailAddress, String fullName) {
        return RegisteredSubscriber.builder()
                .emailAddress(emailAddress)
                .fullName(fullName)
                .build();
    }

    @Override
    @Cacheable(cacheNames = SUBSCRIPTION_CACHE, key = "#emailAddress")
    public RegisteredSubscriber findSubscription(String emailAddress) {
        return null;
    }

    @Override
    @CacheEvict(cacheNames = SUBSCRIPTION_CACHE, key = "#emailAddress")
    public void cancelSubscription(String emailAddress) {

    }
}
