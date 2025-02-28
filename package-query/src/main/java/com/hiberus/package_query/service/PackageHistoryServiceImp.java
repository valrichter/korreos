package com.hiberus.package_query.service;

import com.hiberus.package_query.dto.PackageHistoryDTO;
import com.hiberus.package_query.repository.PackageHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PackageHistoryServiceImp implements PackageHistoryService {

    @Autowired
    private PackageHistoryRepository packageHistoryRepository;

    @Override
    public void savePackageHistory(PackageHistoryDTO packageHistoryDTO) {
        log.info("SERVICE - creating new package history " + "with id : '{}'", packageHistoryDTO.getPackageId());
        packageHistoryRepository.savePackageHistory(packageHistoryDTO);
    }

    @Override
    public PackageHistoryDTO getHistoryByPackageId(String packageId) {
        log.info("SERVICE - get package history with id : '{}'", packageId);
        return packageHistoryRepository.getHistoryByPackageId(packageId);
    }

    @Override
    public List<PackageHistoryDTO> getAllHistoriesPackages() {
        log.info("SERVICE - get all package history");
        return packageHistoryRepository.getAllHistoriesPackages();
    }
}
