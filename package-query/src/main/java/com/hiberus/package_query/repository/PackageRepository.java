package com.hiberus.package_query.repository;

import com.hiberus.model.PackageModel;
import com.hiberus.package_query.dto.PackageDTO;

import java.util.List;

public interface PackageRepository {
    PackageModel getPackageById(String id);
    List<PackageModel> getAllPackages();
}
