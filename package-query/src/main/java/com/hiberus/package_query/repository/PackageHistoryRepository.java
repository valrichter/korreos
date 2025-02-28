package com.hiberus.package_query.repository;

import com.hiberus.package_query.dto.PackageHistoryDTO;

import java.util.List;

public interface PackageHistoryRepository {
    void savePackageHistory(PackageHistoryDTO packageHistoryDTO);
    PackageHistoryDTO getHistoryByPackageId(String packageId);
    List<PackageHistoryDTO> getAllHistoriesPackages();
}
