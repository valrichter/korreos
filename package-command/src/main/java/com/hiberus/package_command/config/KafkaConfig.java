package com.hiberus.package_command.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Value("${environment.warehouse-transfers-topic}")
    private String warehouseTransfersTopicName;

    @Value("${environment.riders-data-topic}")
    private String ridersDataTopicName;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put("bootstrap.servers", "localhost:9092");
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic warehouseTransfersTopic() {
        return TopicBuilder.name(warehouseTransfersTopicName)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic ridersDataTopic() {
        return TopicBuilder.name(ridersDataTopicName)
                .partitions(1)
                .replicas(1)
                .build();
    }
}