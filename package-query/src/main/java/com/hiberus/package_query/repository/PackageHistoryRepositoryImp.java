package com.hiberus.package_query.repository;

import com.hiberus.correos.avro.PackageHistoryValue;
import com.hiberus.correos.avro.WarehouseHistory;
import com.hiberus.package_query.dto.PackageHistoryDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
@Slf4j
public class PackageHistoryRepositoryImp implements PackageHistoryRepository {

    @Autowired
    @Qualifier("packageHistoryRedisTemplate")
    private RedisTemplate<String, PackageHistoryDTO> redisTemplate;

    public void savePackageHistory(PackageHistoryDTO packageHistoryDTO) {
        log.info("REPOSITORY - saving new package history " + "with package_id : '{}'",
                packageHistoryDTO.getPackageId());
        this.convertAvroArrayToList(packageHistoryDTO);
        redisTemplate.opsForValue().set(packageHistoryDTO.getPackageId(), packageHistoryDTO);
    }

    private void convertAvroArrayToList(PackageHistoryDTO dto) {
        if (dto.getHistory() instanceof GenericData.Array) {
            GenericData.Array<?> avroArray = (GenericData.Array<?>) dto.getHistory();

            List<PackageHistoryDTO.WarehouseHistoryDTO> locations = new ArrayList<>();

            for (Object obj : avroArray) {
                if (obj instanceof WarehouseHistory) {
                    WarehouseHistory warehouseHistory = (WarehouseHistory) obj;

                    // Convert each Avro WarehouseHistory to a WarehouseHistoryDTO
                    PackageHistoryDTO.WarehouseHistoryDTO warehouseHistoryDTO = new PackageHistoryDTO.WarehouseHistoryDTO();
                    warehouseHistoryDTO.setWarehouseName(warehouseHistory.getWarehouseName());
                    warehouseHistoryDTO.setRiderName(warehouseHistory.getRiderName());

                    // Add to the list
                    locations.add(warehouseHistoryDTO);
                }
            }

            // Set the list in the DTO
            dto.setHistory(locations);
        }
    }

    public PackageHistoryDTO getHistoryByPackageId(String packageId) {
        return redisTemplate.opsForValue().get(packageId);
    }

    public List<PackageHistoryDTO> getAllHistoriesPackages() {
        Set<String> keys = redisTemplate.keys("*");
        List<PackageHistoryDTO> packages = new ArrayList<>();
        for (String key : keys) {
            packages.add(redisTemplate.opsForValue().get(key));
        }
        return packages;
    }

}
