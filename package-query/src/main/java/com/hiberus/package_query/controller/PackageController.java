package com.hiberus.package_query.controller;

import com.hiberus.package_query.dto.PackageDTO;
import com.hiberus.package_query.dto.PackageHistoryDTO;
import com.hiberus.package_query.service.PackageHistoryService;
import com.hiberus.package_query.service.PackageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v0")
public class PackageController {

    @Autowired
    PackageService packageService;

    @Autowired
    PackageHistoryService packageHistoryService;

    @GetMapping ("/package")
    public ResponseEntity<List<PackageDTO>> getAllPackages() {
        log.info("CONTROLLER - get all packages");
        List<PackageDTO> allPackagesDTO = packageService.getAllPackages();
        return ResponseEntity.status(HttpStatus.OK).body(allPackagesDTO);
    }

    @GetMapping ("/package/{packageId}")
    public ResponseEntity<PackageDTO> getPackageById(@PathVariable String packageId) {
        log.info("CONTROLLER - get package with id: {}", packageId);
        PackageDTO packageDTO = packageService.getPackageById(packageId);
        return ResponseEntity.status(HttpStatus.OK).body(packageDTO);
    }

    @GetMapping ("/package/{packageId}/history")
    public ResponseEntity<PackageHistoryDTO> getHistoryByPackageId(@PathVariable String packageId) {
        log.info("CONTROLLER - get package history with id: {}", packageId);
        PackageHistoryDTO t =   packageHistoryService.getHistoryByPackageId(packageId);
        return ResponseEntity.status(HttpStatus.OK).body(t);
    }

    @GetMapping ("/package/history")
    public ResponseEntity<List<PackageHistoryDTO>> getAllHistoriesPackages() {
        log.info("CONTROLLER - get all histories");
        List<PackageHistoryDTO> t = packageHistoryService.getAllHistoriesPackages();
        return ResponseEntity.status(HttpStatus.OK).body(t);
    }
}
