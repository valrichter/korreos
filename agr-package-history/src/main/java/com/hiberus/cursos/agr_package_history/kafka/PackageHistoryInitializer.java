package com.hiberus.cursos.agr_package_history.kafka;

import com.hiberus.correos.avro.PackageHistoryValue;
import com.hiberus.correos.avro.Status;
import org.apache.kafka.streams.kstream.Initializer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class PackageHistoryInitializer implements Initializer<PackageHistoryValue> {
    @Override
    public PackageHistoryValue apply() {
        return PackageHistoryValue.newBuilder()
                .setPackageId("")
                .setHistory(new ArrayList<>())
                .setStatus(Status.AT_WAREHOUSE)
                .build();
    }
}
