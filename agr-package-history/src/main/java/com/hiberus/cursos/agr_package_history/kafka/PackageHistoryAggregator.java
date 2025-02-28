package com.hiberus.cursos.agr_package_history.kafka;

import com.hiberus.correos.avro.PackageAndRiderValue;
import com.hiberus.correos.avro.PackageHistoryKey;
import com.hiberus.correos.avro.PackageHistoryValue;
import com.hiberus.correos.avro.WarehouseHistory;
import com.hiberus.model.PackageModel;
import org.apache.kafka.streams.kstream.Aggregator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PackageHistoryAggregator implements Aggregator<PackageHistoryKey, PackageAndRiderValue, PackageHistoryValue> {

    @Override
    public PackageHistoryValue apply(PackageHistoryKey packageHistoryKey,
                                     PackageAndRiderValue packageAndRiderValue,
                                     PackageHistoryValue packageHistoryValue) {

        List<WarehouseHistory> currentHistory = packageHistoryValue.getHistory();
        if (currentHistory == null) {
            currentHistory = new ArrayList<>();
        }

        List<WarehouseHistory> updatedHistory = new ArrayList<>(currentHistory);

        // Si el paquete está en un almacén, agrega un nuevo registro
        if (packageAndRiderValue.getStatus().name().equals(PackageModel.Status.AT_WAREHOUSE.name())) {
            WarehouseHistory newWarehouseHistory = WarehouseHistory.newBuilder()
                    .setWarehouseName(packageAndRiderValue.getActualWarehouse())
                    .setRiderName(packageAndRiderValue.getRiderName())
                    .build();
            updatedHistory.add(newWarehouseHistory);
        }

        // Devuelve el nuevo PackageHistoryValue con el historial actualizado
        return PackageHistoryValue.newBuilder()
                .setPackageId(packageHistoryKey.getPackageId())
                .setHistory(updatedHistory)
                .setStatus(packageAndRiderValue.getStatus())
                .build();
    }
}
