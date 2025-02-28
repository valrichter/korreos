package com.hiberus.pcs_destiny_eta.config;

import com.hiberus.correos.avro.PackageDestinyETAKey;
import com.hiberus.correos.avro.PackageDestinyETAValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaPackageDestinyEtaProducer {

    @Value("${environment.package-destiny-ETA-topic}")
    private String packageDestinyETATopic;

    @Autowired
    KafkaTemplate<PackageDestinyETAKey, PackageDestinyETAValue> kafkaTemplate;

    public void produce(PackageDestinyETAKey packageDestinyETAKey, PackageDestinyETAValue packageDestinyETAValue) {
        log.debug("Sending record to topic '{}' with key: '{}', value: '{}'",
                packageDestinyETATopic, packageDestinyETAKey, packageDestinyETAValue);

        kafkaTemplate.send(packageDestinyETATopic, packageDestinyETAKey, packageDestinyETAValue);
    }
}
