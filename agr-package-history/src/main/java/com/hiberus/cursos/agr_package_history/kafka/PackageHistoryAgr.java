package com.hiberus.cursos.agr_package_history.kafka;

import com.hiberus.correos.avro.PackageAndRiderKey;
import com.hiberus.correos.avro.PackageAndRiderValue;
import com.hiberus.correos.avro.PackageHistoryKey;
import com.hiberus.correos.avro.PackageHistoryValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
@Slf4j
public class PackageHistoryAgr {

    @Autowired
    private PackageHistoryInitializer packageHistoryInitializer;

    @Autowired
    private PackageHistoryAggregator packageHistoryAggregator;

    @Bean
    public Function<KStream<PackageAndRiderKey, PackageAndRiderValue>, KStream<PackageHistoryKey, PackageHistoryValue>> aggregatePackageHistory() {
        return comprasStream -> comprasStream
                .peek((k, v) -> log.info("AGGREGATE - Received message with key: {} and value {}", k, v))
                .selectKey((k, v) -> PackageHistoryKey.newBuilder().setPackageId(v.getPackageId()).build())
                .groupByKey()
                .aggregate(packageHistoryInitializer, packageHistoryAggregator
                )
                .toStream()
                .peek((k, v) -> log.info("AGGREGATE - Sending message with key: {} and value {}", k, v));
    }
}