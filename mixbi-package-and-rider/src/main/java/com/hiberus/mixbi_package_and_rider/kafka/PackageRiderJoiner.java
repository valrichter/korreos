package com.hiberus.mixbi_package_and_rider.kafka;

import com.hiberus.correos.avro.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Named;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

@Configuration
@Slf4j
public class PackageRiderJoiner {

    @Bean
    public BiFunction<KStream<PackageDestinyETAKey, PackageDestinyETAValue>, KStream<RidersDataKey, RidersDataValue>,
            KStream<PackageAndRiderKey, PackageAndRiderValue>> joiner() {
        return (packageDestinyETAStream, ridersStream) -> {
            KTable<PackageAndRiderKey, PackageDestinyETAValue> packageDestinyETAValueKTable = packageDestinyETAStream
                    .selectKey((key, value) -> PackageAndRiderKey.newBuilder()
                            .setRiderId(value.getRiderId())
                            .build())
                    .groupByKey()
                    .reduce((aggValue, newValue) -> {
                        if (aggValue.equals(newValue)) {
                            return aggValue;
                        }
                        return newValue;
                    })
                    .toStream()
                    .toTable(Named.as("PACKAGE_DESTINY_ETA_TABLE"), Materialized.as("PACKAGE_DESTINY_ETA_TABLE"));
            
            KTable<PackageAndRiderKey, RidersDataValue> ridersKTable = ridersStream
                    .selectKey((key, value) -> PackageAndRiderKey.newBuilder()
                            .setRiderId(value.getRiderId())
                            .build())
                    .groupByKey()
                    .reduce((aggValue, newValue) -> {
                        if (aggValue.equals(newValue)) {
                            return aggValue;
                        }
                        return newValue;
                    })
                    .toStream()
                    .toTable(Named.as("RIDERS_TABLE"), Materialized.as("RIDERS_TABLE"));

            return packageDestinyETAValueKTable.join(ridersKTable,
                            (packageDestinyETAValue, ridersValue) -> PackageAndRiderValue.newBuilder()
                                    .setPackageId(packageDestinyETAValue.getPackageId())
                                    .setRiderId(packageDestinyETAValue.getRiderId())
                                    .setRiderName(ridersValue.getName())
                                    .setPreviousWarehouse(packageDestinyETAValue.getPreviousWarehouse())
                                    .setActualWarehouse(packageDestinyETAValue.getActualWarehouse())
                                    .setDestinyWarehouse(packageDestinyETAValue.getDestinyWarehouse())
                                    .setDestinyEta(packageDestinyETAValue.getDestinyEta())
                                    .setStatus(Status.valueOf(packageDestinyETAValue.getStatus().name()))
                                    .build())
                    .toStream()
                    .filter(this::shouldSendRecord)
                    .peek((k, v) -> log.info("JOINER - sending record to topic '{}' with key: '{}', value: '{}'", "package-and-rider", k, v));
        };
    }

    private final Map<PackageAndRiderKey, PackageAndRiderValue> cache = new ConcurrentHashMap<>();

    private boolean shouldSendRecord(PackageAndRiderKey key, PackageAndRiderValue value) {
        PackageAndRiderValue previous = cache.put(key, value);
        return !value.equals(previous);
    }
}
