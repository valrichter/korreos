package com.hiberus.package_command.service;

import com.hiberus.correos.avro.RidersDataKey;
import com.hiberus.correos.avro.RidersDataValue;
import com.hiberus.package_command.repository.RiderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KafkaRidersDataProducerTest {

    @Mock
    private KafkaTemplate<RidersDataKey, RidersDataValue> kafkaTemplate;

    @Mock
    private RiderRepository riderRepository;

    @InjectMocks
    private KafkaRidersDataProducer kafkaRidersDataProducer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProduce() {
        String riderId = "rider123";
        String riderName = "John Doe";

        when(riderRepository.getRiderById(riderId)).thenReturn(riderName);

        String topicName = "riders-data-topic";
        kafkaRidersDataProducer.ridersDataTopicName = topicName;

        kafkaRidersDataProducer.produce(riderId);

        verify(kafkaTemplate, times(1)).send(
                eq(topicName),
                any(RidersDataKey.class),
                any(RidersDataValue.class)
        );

        RidersDataKey expectedKey = RidersDataKey.newBuilder().setRiderId(riderId).build();
        RidersDataValue expectedValue = RidersDataValue.newBuilder().setRiderId(riderId).setName(riderName).build();

        verify(kafkaTemplate).send(eq(topicName), eq(expectedKey), eq(expectedValue));
    }
}
