package com.hiberus.pcs_destiny_eta.config;

import com.hiberus.correos.avro.PackageDestinyETAKey;
import com.hiberus.correos.avro.PackageDestinyETAValue;
import com.hiberus.correos.avro.WarehouseTransfersKey;
import com.hiberus.correos.avro.WarehouseTransfersValue;
import com.hiberus.model.PackageModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class KafkaWarehouseTransferConsumer {

    @Autowired
    private KafkaPackageDestinyEtaProducer kafkaPackageDestinyEtaProducer;

    @KafkaListener(topics = "${environment.warehouse-transfers-topic}")
    public void consume(ConsumerRecord<WarehouseTransfersKey, WarehouseTransfersValue> warehouseTransfer) {
        log.debug("Received record from topic '{}' in partition '{}' and offset '{}' with key: '{}', value: '{}'",
                warehouseTransfer.topic(), warehouseTransfer.partition(), warehouseTransfer.offset(), warehouseTransfer.key(), warehouseTransfer.value());

        PackageDestinyETAKey packageDestinyETAKey = PackageDestinyETAKey.newBuilder()
                .setActualWarehouse(warehouseTransfer.value().getActualWarehouse())
                .build();

        String originalDestinyWarehouse = warehouseTransfer.value().getDestinyWarehouse();
        String destinyWarehouse = originalDestinyWarehouse != null ? originalDestinyWarehouse : "CORDOBA";

        String destinyEta = warehouseTransfer.value().getDestinyWarehouse() != null
                ? destinyWarehouse + "-ETA : " + LocalDateTime.now()
                : "0";

        PackageDestinyETAValue packageDestinyETAValue = PackageDestinyETAValue.newBuilder()
                .setPackageId(warehouseTransfer.value().getPackageId())
                .setRiderId(warehouseTransfer.value().getRiderId())
                .setPreviousWarehouse(warehouseTransfer.value().getPreviousWarehouse())
                .setActualWarehouse(warehouseTransfer.value().getActualWarehouse())
                .setDestinyWarehouse(destinyWarehouse)
                .setStatus(warehouseTransfer.value().getStatus())
                .setDestinyEta(destinyEta)
                .build();

        kafkaPackageDestinyEtaProducer.produce(packageDestinyETAKey, packageDestinyETAValue);
    }
}
