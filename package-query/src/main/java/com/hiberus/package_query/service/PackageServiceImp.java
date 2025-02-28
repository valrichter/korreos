package com.hiberus.package_query.service;

import com.hiberus.model.PackageModel;
import com.hiberus.package_query.dto.PackageDTO;
import com.hiberus.package_query.mapper.PackageMapper;
import com.hiberus.package_query.repository.PackageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PackageServiceImp implements PackageService {

    @Autowired
    private PackageRepository packageRepository;

    @Override
    public PackageDTO getPackageById(String packageId) {
        log.info("SERVICE - get package with id : '{}'", packageId);
        PackageModel packageModel = packageRepository.getPackageById(packageId);
        return PackageMapper.toDTO(packageModel);
    }

    @Override
    public List<PackageDTO> getAllPackages() {
        log.info("SERVICE - get all packages");
        return packageRepository.getAllPackages()
                .stream()
                .map(PackageMapper::toDTO)
                .collect(Collectors.toList());
    }

}
