package com.hiberus.package_command.repository;

import com.hiberus.model.PackageModel;

public interface PackageRepository {
    PackageModel deletePackageById(String id);
    PackageModel createPackage(PackageModel packageDTO);
    PackageModel updatePackage(PackageModel packageDTO);
    // Agregado para fine mas practicos, deberia llamar al micro de Query
    PackageModel getPackageById(String id);
}
