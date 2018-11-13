package com.tiket.poc.testing.mongodb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author zakyalvan
 */
@DataMongoTest(includeFilters = @ComponentScan.Filter(SubscriptionService.class))
@RunWith(SpringRunner.class)
public class SubscriptionServiceTests {
    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Test
    public void whenSubscribe_thenOneDocumentAddedToCollection() {
        long initialCount = subscriptionRepository.count();

        RegisteredSubscriber subscriber = subscriptionService.registerSubscriber("zaky.alvan@tiket.com", "Muhammad Zaky Alvan");
        RegisteredSubscriber persisted = subscriptionRepository.findOne(subscriber.getId());

        assertThat(subscriptionRepository.count(), is(initialCount + 1));
        assertThat(subscriber.getId(), notNullValue(String.class));
        assertThat(persisted.getEmailAddress(), equalTo(subscriber.getEmailAddress()));
        assertThat(persisted.getFullName(), equalTo(subscriber.getFullName()));
    }
}
