package com.sample.kafka.runner;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 初始化Kafka客户端
 *
 * @author Aaric, created on 2020-05-15T11:32.
 * @version 0.1.0-SNAPSHOT
 */
@Slf4j
@Order(1)
@Component
public class KafkaClientRunner implements CommandLineRunner {

    @Value("${spring.kafka.topic.tbox}")
    private String kafkaTopic;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * 动态创建KafkaListener
     *
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        log.info("kafkaTopic: {}, groupId: {}", kafkaTopic, groupId);

        // consumer
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        consumerProps.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 100);
        consumerProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        ContainerProperties consumerContainerProps = new ContainerProperties(kafkaTopic);
        consumerContainerProps.setMessageListener((MessageListener<Integer, String>) data -> {
            log.debug("received -> {}", data);
        });

        DefaultKafkaConsumerFactory<Integer, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps);
        KafkaMessageListenerContainer<Integer, String> consumerListenerContainer = new KafkaMessageListenerContainer<>(consumerFactory, consumerContainerProps);
        consumerListenerContainer.setBeanName("consumerListenerContainer");
        consumerListenerContainer.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            consumerListenerContainer.stop();
        }));

        // producer
//        Map<String, Object> producerProps = new HashMap<>();
//        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        producerProps.put(ProducerConfig.RETRIES_CONFIG, 0);
//        producerProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
//        producerProps.put(ProducerConfig.LINGER_MS_CONFIG, 1);
//        producerProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
//        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
//        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//
//        ProducerFactory<Integer, String> producerFactory = new DefaultKafkaProducerFactory<>(producerProps);
//        KafkaTemplate<Integer, String> kafkaTemplate = new KafkaTemplate<>(producerFactory);
//
//        kafkaTemplate.send(kafkaTopic, "hello world");
//        kafkaTemplate.flush();
    }
}
