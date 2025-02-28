package com.hiberus.package_query.service;

import com.hiberus.package_query.dto.PackageDTO;

import java.util.List;

public interface PackageService {
    PackageDTO getPackageById(String packageId);
    List<PackageDTO> getAllPackages();
}
