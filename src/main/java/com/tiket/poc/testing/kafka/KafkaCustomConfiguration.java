package com.tiket.poc.testing.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

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
    public KafkaTemplate<?, ?> kafkaTemplate(
            ProducerFactory<Object, Object> kafkaProducerFactory,
            ProducerListener<Object, Object> kafkaProducerListener) {
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
        kafkaTemplate.setProducerListener(kafkaProducerListener);
        kafkaTemplate.setDefaultTopic(this.properties.getTemplate().getDefaultTopic());
        kafkaTemplate.setMessageConverter(messageConverter());
        return kafkaTemplate;
    }

    @Bean
    RecordMessageConverter messageConverter() {
        StringJsonMessageConverter converter = new StringJsonMessageConverter(objectMapper);
        return converter;
    }
}
