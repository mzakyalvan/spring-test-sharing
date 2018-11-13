package com.tiket.poc.testing.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.ConversionException;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.messaging.Message;

/**
 * @author zakyalvan
 */
@Configuration
public class KafkaCustomConfiguration {
    private final KafkaProperties properties;

    private final ObjectMapper objectMapper;

    public KafkaCustomConfiguration(KafkaProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Bean
    public KafkaTemplate<Object, Object> kafkaTemplate(ProducerFactory<Object, Object> producerFactory,
                                                       ProducerListener<Object, Object> producerListener) {
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setProducerListener(producerListener);
        kafkaTemplate.setDefaultTopic(this.properties.getTemplate().getDefaultTopic());
        kafkaTemplate.setMessageConverter(messageConverter());
        return kafkaTemplate;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory(ConsumerFactory<Object, Object> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> listenerFactory = new ConcurrentKafkaListenerContainerFactory<>();

        KafkaProperties.Listener listenerProperties = properties.getListener();

        listenerFactory.setConsumerFactory(consumerFactory);
        listenerFactory.setMessageConverter(messageConverter());

        ContainerProperties containerProperties = listenerFactory.getContainerProperties();

        if (listenerProperties.getAckMode() != null) {
            containerProperties.setAckMode(listenerProperties.getAckMode());
        }
        if (listenerProperties.getAckCount() != null) {
            containerProperties.setAckCount(listenerProperties.getAckCount());
        }
        if (listenerProperties.getAckTime() != null) {
            containerProperties.setAckTime(listenerProperties.getAckTime());
        }
        if (listenerProperties.getPollTimeout() != null) {
            containerProperties.setPollTimeout(listenerProperties.getPollTimeout());
        }
        if (listenerProperties.getConcurrency() != null) {
            listenerFactory.setConcurrency(listenerProperties.getConcurrency());
        }
        return listenerFactory;
    }

    @Bean
    RecordMessageConverter messageConverter() {
        StringJsonMessageConverter converter = new StringJsonMessageConverter(objectMapper) {
            @Override
            protected Object convertPayload(Message<?> message) {
                try {
                    return objectMapper.writeValueAsBytes(message.getPayload());
                }
                catch (JsonProcessingException jpe) {
                    throw new ConversionException("Failed to convert to JSON (byte array)", jpe);
                }
            }
        };
        return converter;
    }
}
