package com.oyo.paisa.kafkaConfi;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConsumerConfiguration {
    @Value("${kafka.bootstrap.server: localhost:9092}")
    private String bootstrapServer;

    @Value("${kafka.consumer.groupId: kafkaConsumerGroup}")
    private String consumerGroup;

    @Value("${kafka.consumer.default_api_timeout:1800000}")
    private String default_api_timeout;

    @Value("${kafka.consumer.max_poll_interval:1800000}")
    private String max_poll_interval;

    @Value("${kafka.consumer.concurrency:5}")
    private String kafkaConcurrency;




    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, default_api_timeout);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, max_poll_interval);
        return props;
    }

    @Bean
    public ConsumerFactory<Object, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(Integer.parseInt(kafkaConcurrency));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setMissingTopicsFatal(false);
        return factory;
    }

}