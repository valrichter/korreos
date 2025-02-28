package com.hiberus.package_query.service;

import com.hiberus.correos.avro.PackageHistoryKey;
import com.hiberus.correos.avro.PackageHistoryValue;
import com.hiberus.model.PackageModel;
import com.hiberus.package_query.dto.PackageHistoryDTO;
import com.hiberus.package_query.repository.PackageHistoryRepositoryImp;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KafkaPackageHistoryConsumer {

    @Value("${environment.package-history-topic}")
    private String packageHistoryTopicName;

    @Autowired
    private PackageHistoryRepositoryImp topicRepository;

    @KafkaListener(topics = "${environment.package-history-topic}")
    public void consume(ConsumerRecord<PackageHistoryKey, PackageHistoryValue> packageHistoryRecord) {
        log.info("Received record from topic '{}' in partition '{}' and offset '{}' with key: '{}', value: '{}'",
                packageHistoryRecord.topic(), packageHistoryRecord.partition(),
                packageHistoryRecord.offset(), packageHistoryRecord.key(), packageHistoryRecord.value());

        PackageHistoryKey key = packageHistoryRecord.key();
        PackageHistoryValue value = packageHistoryRecord.value();

        PackageHistoryDTO packageHistoryDTO = new PackageHistoryDTO();
        packageHistoryDTO.setPackageId(key.getPackageId());

        List<PackageHistoryDTO.WarehouseHistoryDTO> warehouseHistoryDTOList = value.getHistory().stream()
                .map(warehouseHistory -> {
                    PackageHistoryDTO.WarehouseHistoryDTO dto = new PackageHistoryDTO.WarehouseHistoryDTO();
                    dto.setWarehouseName(warehouseHistory.getWarehouseName());
                    dto.setRiderName(warehouseHistory.getRiderName());
                    return dto;
                })
                .collect(Collectors.toList());

        packageHistoryDTO.setHistory(warehouseHistoryDTOList);
        packageHistoryDTO.setStatus(PackageModel.Status.valueOf(value.getStatus().name())); // Map enum to DTO enum

        topicRepository.savePackageHistory(packageHistoryDTO);
    }


}
