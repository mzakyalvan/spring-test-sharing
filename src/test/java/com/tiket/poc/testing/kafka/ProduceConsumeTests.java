package com.tiket.poc.testing.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.messaging.support.MessageBuilder.*;

/**
 * @author zakyalvan
 */
@SpringBootTest(classes = KafkaTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
                "spring.kafka.client-id=testing-poc",
                "spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.ByteArraySerializer",
                "spring.kafka.template.default-topic=subscribe.register",
                "spring.kafka.consumer.group-id=testing-poc-group",
                "spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.ByteArrayDeserializer",
                "spring.kafka.listener.concurrency=5"
        })
@EmbeddedKafka(topics = ProduceConsumeTests.SUBSCRIBE_TOPIC,
        partitions = ProduceConsumeTests.PARTITION_COUNT,
        count = 3
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@RunWith(SpringRunner.class)
public class ProduceConsumeTests {
    public static final String SUBSCRIBE_TOPIC = "subscribe.register";
    public static final int PARTITION_COUNT = 3;

    @Autowired
    private KafkaListenerEndpointRegistry endpointRegistry;

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private SubscriptionListener subscriptionListener;

    @Before
    public void setUp() throws Exception {
        /**
         * Wait all listener containers to be ready for message receive and processing.
         */
        for(Object listenerContainer : endpointRegistry.getListenerContainers()) {
            ContainerTestUtils.waitForAssignment(listenerContainer, PARTITION_COUNT);
        }
    }

    @Test
    public void whenPublishSubscription_thenListenerShouldInvoked() throws Exception {
        ArgumentCaptor<SubscriptionData> subscriptionCaptor = ArgumentCaptor.forClass(SubscriptionData.class);
        doAnswer(invocation -> invocation.callRealMethod())
                .when(subscriptionListener).handleSubscription(subscriptionCaptor.capture());

        LocalDateTime subscribeTime = LocalDateTime.now();

        Message<SubscriptionData> producerMessage = withPayload(
                SubscriptionData.builder()
                        .emailAddress("zaky.alvan@tiket.com")
                        .fullName("Muhammad Zaky Alvan")
                        .subscribeTime(subscribeTime)
                        .build()
                )
                .build();

        CountDownLatch sendLatch = new CountDownLatch(1);
        kafkaTemplate.send(producerMessage).addCallback(result -> sendLatch.countDown(), Throwable::printStackTrace);
        sendLatch.await(200, TimeUnit.MILLISECONDS);

        Thread.sleep(100);

        verify(subscriptionListener, times(1))
                .handleSubscription(Mockito.any(SubscriptionData.class));

        assertThat(subscriptionCaptor.getValue(), allOf(
                instanceOf(SubscriptionData.class),
                hasProperty("emailAddress", equalTo("zaky.alvan@tiket.com")),
                hasProperty("fullName", equalTo("Muhammad Zaky Alvan")),
                hasProperty("subscribeTime", equalTo(subscribeTime))
        ));
    }
}
