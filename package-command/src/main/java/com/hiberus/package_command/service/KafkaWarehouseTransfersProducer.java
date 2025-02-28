package com.hiberus.package_command.service;

import com.hiberus.correos.avro.Status;
import com.hiberus.correos.avro.WarehouseTransfersKey;
import com.hiberus.correos.avro.WarehouseTransfersValue;
import com.hiberus.package_command.dto.PackageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaWarehouseTransfersProducer {

    @Autowired
    private KafkaTemplate<WarehouseTransfersKey, WarehouseTransfersValue> kafkaTemplate;

    @Value("${environment.warehouse-transfers-topic}")
    String warehouseTransfersTopicName;

    public void produce(PackageDTO packageCreateDTO) {
        log.info("WAREHOUSE TRANSFERS PRODUCER - sending record to topic '{}' with key: '{}', value: '{}'", warehouseTransfersTopicName,
                packageCreateDTO.getId(), packageCreateDTO);
        WarehouseTransfersKey key = WarehouseTransfersKey.newBuilder()
                .setPackageId(packageCreateDTO.getId())
                .build();
        WarehouseTransfersValue value = WarehouseTransfersValue.newBuilder()
                .setPackageId(packageCreateDTO.getId())
                .setRiderId(packageCreateDTO.getRiderId())
                .setPreviousWarehouse(packageCreateDTO.getPreviousWarehouse())
                .setActualWarehouse(packageCreateDTO.getActualWarehouse())
                .setDestinyWarehouse(packageCreateDTO.getDestinyWarehouse())
                .setStatus(Status.valueOf(packageCreateDTO.getStatus().name()))
                .build();

        kafkaTemplate.send(warehouseTransfersTopicName, key, value);
    }
}
