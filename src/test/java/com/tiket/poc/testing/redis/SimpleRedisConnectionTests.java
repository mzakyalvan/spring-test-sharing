package com.tiket.poc.testing.redis;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.SocketUtils;
import redis.embedded.RedisServer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author zakyalvan
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class SimpleRedisConnectionTests {
    private static RedisServer redisServer;

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @BeforeClass
    public static void setUp() {
        int redisPort = SocketUtils.findAvailableTcpPort(5555);
        System.setProperty("SPRING_REDIS_PORT", String.valueOf(redisPort));

        redisServer = RedisServer.builder().port(redisPort).build();
        redisServer.start();
    }

    @Test
    public void whenSetAndGet_thenShouldConsistent() {
        connectionFactory.getConnection().set("key".getBytes(), "value".getBytes());
        String retrieved = new String(connectionFactory.getConnection().get("key".getBytes()));
        assertThat(retrieved, is("value"));
    }

    @Test
    public void whenTtlElapsed_thenShouldDisappear() throws Exception {
        connectionFactory.getConnection().set("key".getBytes(), "value".getBytes());
        connectionFactory.getConnection().pExpire("key".getBytes(), 300);
        Thread.sleep(300);
        assertThat(connectionFactory.getConnection().get("key".getBytes()), nullValue());
    }

    @AfterClass
    public static void tearDownClass() {
        redisServer.stop();
    }
}
