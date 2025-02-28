package com.hiberus.package_command.service;

import com.hiberus.correos.avro.*;
import com.hiberus.package_command.repository.RiderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaRidersDataProducer {

    @Value("${environment.riders-data-topic}")
    String ridersDataTopicName;

    @Autowired
    private KafkaTemplate<RidersDataKey, RidersDataValue> kafkaTemplate;

    @Autowired
    private RiderRepository riderRepository;

    public void produce(String riderId) {

        String riderName = riderRepository.getRiderById(riderId);

        RidersDataKey key = RidersDataKey.newBuilder()
                .setRiderId(riderId)
                .build();
        RidersDataValue value = RidersDataValue.newBuilder()
                .setRiderId(riderId)
                .setName(riderName)
                .build();

        log.info("RIDERS DATA PRODUCER - sending record to topic '{}' with key: '{}', value: '{}'",
                ridersDataTopicName, key, value);
        kafkaTemplate.send(ridersDataTopicName, key, value);
    }

}
