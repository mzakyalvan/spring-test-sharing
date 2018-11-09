package com.tiket.poc.testing.redis;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.SocketUtils;
import redis.embedded.RedisServer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author zakyalvan
 */
@SpringBootTest(classes = RedisTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
            "spring.cache.type=redis",
        })
@RunWith(SpringRunner.class)
public class SimpleRedisCacheTest {
    private static RedisServer redisServer;

    @Autowired
    private SubscriptionService subscriptionService;

    @BeforeClass
    public static void setUpClass() {
        int redisPort = SocketUtils.findAvailableTcpPort(5555);
        System.setProperty("SPRING_REDIS_PORT", String.valueOf(redisPort));

        redisServer = RedisServer.builder().port(redisPort).build();
        redisServer.start();
    }

    @Test
    public void whenRetrieveEvictedKey_thenMustReturnNull() {
        subscriptionService.registerSubscriber("zaky.alvan@tiket.com", "Muhammad Zaky Alvan");
        subscriptionService.cancelSubscription("zaky.alvan@tiket.com");
        RegisteredSubscriber subscriber = subscriptionService.findSubscription("zaky.alvan@tiket.com");
        assertThat(subscriber, nullValue());
    }

    @Test
    public void whenRetrieveCachedKey_thenMustCached() {
        subscriptionService.registerSubscriber("zaky.alvan@tiket.com", "Muhammad Zaky Alvan");
        RegisteredSubscriber subscriber  = subscriptionService.findSubscription("zaky.alvan@tiket.com");
        assertThat(subscriber.getEmailAddress(), is("zaky.alvan@tiket.com"));
        assertThat(subscriber.getFullName(), is("Muhammad Zaky Alvan"));
    }

    @AfterClass
    public static void tearDownClass() {
        redisServer.stop();
    }
}
