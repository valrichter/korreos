package com.hiberus.package_query.service;

import com.hiberus.package_query.dto.PackageHistoryDTO;

import java.util.List;

public interface PackageHistoryService {
    void savePackageHistory(PackageHistoryDTO packageHistoryDTO);
    PackageHistoryDTO getHistoryByPackageId(String packageId);
    List<PackageHistoryDTO> getAllHistoriesPackages();
}
