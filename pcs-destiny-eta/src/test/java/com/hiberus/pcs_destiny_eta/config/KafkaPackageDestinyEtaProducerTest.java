package com.hiberus.pcs_destiny_eta.config;

import com.hiberus.correos.avro.PackageDestinyETAKey;
import com.hiberus.correos.avro.PackageDestinyETAValue;
import com.hiberus.correos.avro.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class KafkaPackageDestinyEtaProducerTest {

    @Mock
    private KafkaTemplate<PackageDestinyETAKey, PackageDestinyETAValue> kafkaTemplate;

    @InjectMocks
    private KafkaPackageDestinyEtaProducer kafkaPackageDestinyEtaProducer;

    private final String packageDestinyETATopic = "test-package-destiny-eta-topic";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        kafkaPackageDestinyEtaProducer = new KafkaPackageDestinyEtaProducer();
        kafkaPackageDestinyEtaProducer.kafkaTemplate = kafkaTemplate;

        try {
            var field = KafkaPackageDestinyEtaProducer.class.getDeclaredField("packageDestinyETATopic");
            field.setAccessible(true);
            field.set(kafkaPackageDestinyEtaProducer, packageDestinyETATopic);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set the topic value", e);
        }
    }

    @Test
    void testProduce() {
        PackageDestinyETAKey testKey = PackageDestinyETAKey.newBuilder()
                .setActualWarehouse("warehouse-B")
                .build();

        PackageDestinyETAValue testValue = PackageDestinyETAValue.newBuilder()
                .setPackageId("package-123")
                .setRiderId("rider-456")
                .setPreviousWarehouse("warehouse-A")
                .setActualWarehouse("warehouse-B")
                .setDestinyWarehouse("warehouse-C")
                .setStatus(Status.IN_TRANSIT)
                .setDestinyEta("warehouse-C-ETA : 2025-01-01T10:00:00")
                .build();

        // Act: Call the method under test
        kafkaPackageDestinyEtaProducer.produce(testKey, testValue);

        ArgumentCaptor<PackageDestinyETAKey> keyCaptor = ArgumentCaptor.forClass(PackageDestinyETAKey.class);
        ArgumentCaptor<PackageDestinyETAValue> valueCaptor = ArgumentCaptor.forClass(PackageDestinyETAValue.class);

        verify(kafkaTemplate, times(1))
                .send(eq(packageDestinyETATopic), keyCaptor.capture(), valueCaptor.capture());

        PackageDestinyETAKey capturedKey = keyCaptor.getValue();
        PackageDestinyETAValue capturedValue = valueCaptor.getValue();

        assertEquals("warehouse-C", capturedKey.getActualWarehouse());

        assertEquals("package-123", capturedValue.getPackageId());
        assertEquals("rider-456", capturedValue.getRiderId());
        assertEquals("warehouse-A", capturedValue.getPreviousWarehouse());
        assertEquals("warehouse-B", capturedValue.getActualWarehouse());
        assertEquals("warehouse-C", capturedValue.getDestinyWarehouse());
        assertEquals(Status.IN_TRANSIT, capturedValue.getStatus());
        assertEquals("warehouse-C-ETA : 2025-01-01T10:00:00", capturedValue.getDestinyEta());
    }
}
