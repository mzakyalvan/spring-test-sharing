package com.tiket.poc.testing.mongodb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

/**
 * @author zakyalvan
 */
@Slf4j
@Service
@Validated
class MongoSubscriptionService implements SubscriptionService {

    private final SubscriptionRepository subscriberRepository;

    public MongoSubscriptionService(SubscriptionRepository subscriberRepository) {
        Assert.notNull(subscriberRepository, "Repository must be provided");
        this.subscriberRepository = subscriberRepository;
    }

    @Override
    public RegisteredSubscriber registerSubscriber(String emailAddress, String fullName) {
        return subscriberRepository.save(RegisteredSubscriber.builder()
                .emailAddress(emailAddress).fullName(fullName).subscribedTime(LocalDateTime.now())
                .build());
    }
}
