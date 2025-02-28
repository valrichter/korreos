package com.hiberus.pcs_destiny_eta;

import com.hiberus.correos.avro.PackageDestinyETAKey;
import com.hiberus.correos.avro.PackageDestinyETAValue;
import com.hiberus.correos.avro.WarehouseTransfersKey;
import com.hiberus.correos.avro.WarehouseTransfersValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;

@Configuration
@SpringBootApplication
@Slf4j
public class PcsDestinyEtaApplication {

    public static void main(final String[] args) {
        SpringApplication.run(PcsDestinyEtaApplication.class, args);
    }

}
