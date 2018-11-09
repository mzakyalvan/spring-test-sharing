package com.tiket.poc.testing.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author zakyalvan
 */
public interface SubscriptionRepository extends MongoRepository<RegisteredSubscriber, String> {
}
