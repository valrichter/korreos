package com.hiberus.pcs_destiny_eta.config;

import com.hiberus.correos.avro.PackageDestinyETAKey;
import com.hiberus.correos.avro.PackageDestinyETAValue;
import com.hiberus.correos.avro.WarehouseTransfersKey;
import com.hiberus.correos.avro.WarehouseTransfersValue;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static com.hiberus.correos.avro.Status.IN_TRANSIT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class KafkaWarehouseTransferConsumerTest {

    @Mock
    private KafkaPackageDestinyEtaProducer kafkaPackageDestinyEtaProducer;

    @InjectMocks
    private KafkaWarehouseTransferConsumer kafkaWarehouseTransferConsumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConsumeWarehouseTransfers() {
        WarehouseTransfersKey warehouseTransfersKey = WarehouseTransfersKey.newBuilder()
                .setPackageId("package-123")
                .build();

        WarehouseTransfersValue warehouseTransfersValue = WarehouseTransfersValue.newBuilder()
                .setPackageId("package-123")
                .setRiderId("rider-456")
                .setPreviousWarehouse("warehouse-A")
                .setActualWarehouse("warehouse-B")
                .setDestinyWarehouse("warehouse-C")
                .setStatus(IN_TRANSIT)
                .build();

        ConsumerRecord<WarehouseTransfersKey, WarehouseTransfersValue> consumerRecord = new ConsumerRecord<>(
                "test-topic",
                0,
                0L,
                warehouseTransfersKey,
                warehouseTransfersValue
        );

        kafkaWarehouseTransferConsumer.consume(consumerRecord);

        ArgumentCaptor<PackageDestinyETAKey> keyCaptor = ArgumentCaptor.forClass(PackageDestinyETAKey.class);
        ArgumentCaptor<PackageDestinyETAValue> valueCaptor = ArgumentCaptor.forClass(PackageDestinyETAValue.class);

        verify(kafkaPackageDestinyEtaProducer, times(1))
                .produce(keyCaptor.capture(), valueCaptor.capture());

        PackageDestinyETAKey capturedKey = keyCaptor.getValue();
        PackageDestinyETAValue capturedValue = valueCaptor.getValue();

        assertEquals("warehouse-C", capturedKey.getActualWarehouse());

        assertEquals("package-123", capturedValue.getPackageId());
        assertEquals("rider-456", capturedValue.getRiderId());
        assertEquals("warehouse-A", capturedValue.getPreviousWarehouse());
        assertEquals("warehouse-B", capturedValue.getActualWarehouse());
        assertEquals("warehouse-C", capturedValue.getDestinyWarehouse());
        assertEquals(IN_TRANSIT, capturedValue.getStatus());
        assertEquals("warehouse-C-ETA",
                capturedValue.getDestinyEta().split(" : ")[0]);
    }
}
