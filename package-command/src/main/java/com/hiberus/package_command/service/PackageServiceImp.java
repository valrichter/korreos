package com.hiberus.package_command.service;

import com.hiberus.model.PackageModel;
import com.hiberus.package_command.dto.PackageDTO;
import com.hiberus.package_command.mapper.PackageMapper;
import com.hiberus.package_command.repository.PackageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class PackageServiceImp implements PackageService {

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private KafkaWarehouseTransfersProducer warehouseTransfersProducer;

    @Autowired
    private KafkaRidersDataProducer ridersDataProducer;

    @Override
    public PackageDTO createPackage(PackageDTO packageDTO) {
        log.info("SERVICE - creating new package with id : '{}'", packageDTO.getId());
        PackageModel packageModel = PackageModel.builder()
                .id(packageDTO.getId())
                .actualWarehouse(packageDTO.getActualWarehouse())
                .status(packageDTO.getStatus())
                .build();
        PackageModel savedPackageModel = packageRepository.createPackage(packageModel);
        return PackageMapper.toDTO(savedPackageModel);
    }

    @Override
    public PackageDTO updatePackage(PackageDTO packageDTO) {
        log.info("SERVICE - updating package with id : '{}'", packageDTO.getId());

        // 1. Obtener el paquete actual desde la base de datos
        PackageModel existingPackage = packageRepository.getPackageById(packageDTO.getId());
        if (existingPackage == null) {
            throw new RuntimeException("Package not found");
        }

        // 2. Validar que el flujo de warehouses sea correcto
        if (!isValidWarehouseTransition(existingPackage, packageDTO)) {
            throw new RuntimeException("Invalid warehouse transition for package ID: " + packageDTO.getId());
        }

        // 3. Convertir DTO a Model y actualizar en la base de datos
        PackageModel packageModel = PackageMapper.toModel(packageDTO);
        PackageModel updatedPackage = packageRepository.updatePackage(packageModel);
        if (updatedPackage == null) {
            throw new RuntimeException("Failed to update package");
        }

        // 4. Convertir Model actualizado a DTO
        PackageDTO updatedPackageDTO = PackageMapper.toDTO(updatedPackage);

        // 5. Publicar eventos en Kafka
        warehouseTransfersProducer.produce(updatedPackageDTO);
        ridersDataProducer.produce(updatedPackageDTO.getRiderId());

        return updatedPackageDTO;
    }

    private boolean isValidWarehouseTransition(PackageModel existing, PackageDTO updated) {
        if (existing.getStatus().equals(updated.getStatus())) {
            log.info("The package is already in: {}", updated.getStatus());
            return false;
        }

        if (PackageModel.Status.IN_TRANSIT.equals(updated.getStatus())) {
            log.info("The new package status is: {}", updated.getStatus());
            return updated.getDestinyWarehouse() != null &&
                    !updated.getDestinyWarehouse().equals(existing.getDestinyWarehouse());
        }

        if (PackageModel.Status.AT_WAREHOUSE.equals(updated.getStatus())) {
            log.info("The new package status is: {}", updated.getStatus());
            return existing.getActualWarehouse().equals(updated.getPreviousWarehouse()) &&
                    existing.getDestinyWarehouse().equals(updated.getActualWarehouse()) &&
                    updated.getDestinyWarehouse() == null;
        }

        return false;
    }

    @Override
    public PackageDTO deletePackageById(String packageId) {
        log.info("SERVICE - deleting package with id : '{}'", packageId);
        PackageModel deletedPackage = packageRepository.deletePackageById(packageId);
        return PackageMapper.toDTO(deletedPackage);
    }

}
