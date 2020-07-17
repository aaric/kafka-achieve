package com.sample.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * Kafka配置
 *
 * @author Aaric, created on 2020-05-15T11:37.
 * @version 0.1.0-SNAPSHOT
 */
@Slf4j
@Configuration
public class KafkaConfig implements InitializingBean {

//    @KafkaListener(topics = "${spring.kafka.topic.tbox}")
//    public void processTopicTBox(String content) {
//        // 打印日志
//        log.debug("content: {}", content);
//
//    }

    @KafkaListener(topics = "${topicName}", concurrency = "${spring.kafka.listener.concurrency}")
    public void processTopicTBox(ConsumerRecord<String, String> record) {
        // 打印日志
        log.info("Record, key: {}, partition: {}, offset: {}, timestamp={}.",
                record.key(), record.partition(), record.offset(), record.timestamp());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.setProperty("topicName", "zs-tbox-01");
    }
}
