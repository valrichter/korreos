package com.hiberus.package_command.service;

import com.hiberus.package_command.dto.PackageDTO;

public interface PackageService {
    PackageDTO createPackage(PackageDTO packageCreateDTO);
    PackageDTO updatePackage(PackageDTO packageCreateDTO);
    PackageDTO deletePackageById(String packageId);
}
