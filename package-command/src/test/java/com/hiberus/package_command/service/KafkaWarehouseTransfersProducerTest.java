package com.hiberus.package_command.service;

import com.hiberus.correos.avro.Status;
import com.hiberus.correos.avro.WarehouseTransfersKey;
import com.hiberus.correos.avro.WarehouseTransfersValue;
import com.hiberus.package_command.dto.PackageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class KafkaWarehouseTransfersProducerTest {

    @Mock
    private KafkaTemplate<WarehouseTransfersKey, WarehouseTransfersValue> kafkaTemplate;

    @InjectMocks
    private KafkaWarehouseTransfersProducer kafkaWarehouseTransfersProducer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProduce() {
        String packageId = "1234";
        PackageDTO packageDTO = new PackageDTO(packageId, "rider1", "warehouse1", "warehouse2", "warehouse3", com.hiberus.model.PackageModel.Status.IN_TRANSIT);

        String topicName = "warehouse-transfers-topic";
        kafkaWarehouseTransfersProducer.warehouseTransfersTopicName = topicName;

        kafkaWarehouseTransfersProducer.produce(packageDTO);

        verify(kafkaTemplate, times(1)).send(
                eq(topicName),
                any(WarehouseTransfersKey.class),
                any(WarehouseTransfersValue.class)
        );
    }
}
