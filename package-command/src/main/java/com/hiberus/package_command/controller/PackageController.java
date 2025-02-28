package com.hiberus.package_command.controller;

import com.hiberus.model.PackageModel;
import com.hiberus.package_command.dto.PackageCreateReq;
import com.hiberus.package_command.dto.PackageDTO;
import com.hiberus.package_command.dto.PackageUpdateReq;
import com.hiberus.package_command.service.PackageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/api/v0")
public class PackageController {

    @Autowired
    PackageService packageService;

    @PostMapping ("/package")
    public ResponseEntity<PackageDTO> createPackage(@Valid @RequestBody PackageCreateReq packageCreateReq) {
        log.info("CONTROLLER - creating new package with id: '{}'", packageCreateReq.getId());
        PackageDTO packageDTO = PackageDTO.builder()
                .id(packageCreateReq.getId())
                .actualWarehouse(packageCreateReq.getActualWarehouse())
                .status(PackageModel.Status.AT_WAREHOUSE)
                .build();
        PackageDTO res = packageService.createPackage(packageDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping ("/package/{packageId}/{status}")
    public ResponseEntity<PackageDTO> updatePackage(
            @Valid @RequestBody PackageUpdateReq packageUpdateReq,
            @PathVariable String packageId,
            @PathVariable PackageModel.Status status) {
        log.debug("CONTROLLER - update package with id: {}", packageId);
        PackageDTO packageDTO = PackageDTO.builder()
                .id(packageId)
                .riderId(packageUpdateReq.getRiderId())
                .previousWarehouse(packageUpdateReq.getPreviousWarehouse())
                .actualWarehouse(packageUpdateReq.getActualWarehouse())
                .destinyWarehouse(packageUpdateReq.getDestinyWarehouse())
                .status(status)
                .build();
        PackageDTO res = packageService.updatePackage(packageDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);
    }

    @DeleteMapping ("/package/{packageId}")
    public ResponseEntity<PackageDTO> deletePackageById(@PathVariable String packageId) {
        log.debug("CONTROLLER - delete package with id: {}", packageId);
        PackageDTO res = packageService.deletePackageById(packageId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);
    }

}
