package com.hiberus.package_command.service;

import com.hiberus.model.PackageModel;
import com.hiberus.package_command.dto.PackageDTO;
import com.hiberus.package_command.mapper.PackageMapper;
import com.hiberus.package_command.repository.PackageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PackageServiceImpTest {

    @Mock
    private PackageRepository packageRepository;

    @Mock
    private KafkaWarehouseTransfersProducer warehouseTransfersProducer;

    @Mock
    private KafkaRidersDataProducer ridersDataProducer;

    @Mock
    private PackageMapper packageMapper;

    @InjectMocks
    private PackageServiceImp packageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePackage() {
        PackageDTO packageDTO = new PackageDTO("1234", "rider1", "warehouse1", "warehouse2", "warehouse3", PackageModel.Status.IN_TRANSIT);
        PackageModel packageModel = PackageModel.builder()
                .id(packageDTO.getId())
                .actualWarehouse(packageDTO.getActualWarehouse())
                .status(packageDTO.getStatus())
                .build();

        when(packageRepository.createPackage(any(PackageModel.class))).thenReturn(packageModel);

        PackageDTO result = packageService.createPackage(packageDTO);

        assertNotNull(result);
        assertEquals(packageDTO.getId(), result.getId());
        assertEquals(packageDTO.getActualWarehouse(), result.getActualWarehouse());
        verify(packageRepository, times(1)).createPackage(any(PackageModel.class));
    }

    @Test
     public void testUpdatePackageSuccess() {
        PackageDTO inputPackageDTO = new PackageDTO();
        inputPackageDTO.setId("1234");
        inputPackageDTO.setPreviousWarehouse("warehouse1");
        inputPackageDTO.setDestinyWarehouse("warehouse2");
        inputPackageDTO.setRiderId("rider1");
        inputPackageDTO.setStatus(PackageModel.Status.AT_WAREHOUSE);

        PackageModel packageModel = new PackageModel();
        packageModel.setId("1234");
        packageModel.setRiderId("rider1");

        PackageModel updatedPackage = new PackageModel();
        updatedPackage.setId("1234");
        updatedPackage.setRiderId("rider1");

        when(packageRepository.updatePackage(any(PackageModel.class))).thenReturn(updatedPackage);

        doNothing().when(warehouseTransfersProducer).produce(any(PackageDTO.class));
        doNothing().when(ridersDataProducer).produce(anyString());

        PackageDTO result = packageService.updatePackage(inputPackageDTO);

        verify(packageRepository).updatePackage(any(PackageModel.class));

        verify(warehouseTransfersProducer).produce(any(PackageDTO.class));
        verify(ridersDataProducer).produce("rider1");

        assertNotNull(result);
        assertEquals("1234", result.getId());
    }

    @Test
    public void testUpdatePackageNotFound() {
        PackageDTO packageDTO = new PackageDTO("1234", "rider1", "warehouse1", "warehouse2", "warehouse3", PackageModel.Status.IN_TRANSIT);

        when(packageRepository.updatePackage(any(PackageModel.class))).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            packageService.updatePackage(packageDTO);
        });
        assertEquals("Package not found", exception.getMessage());
    }

    @Test
    public void testDeletePackage() {
        PackageDTO packageDTO = new PackageDTO("1234", "rider1", "warehouse1", "warehouse2", "warehouse3", PackageModel.Status.IN_TRANSIT);
        PackageModel deletedPackageModel = PackageModel.builder()
                .id(packageDTO.getId())
                .build();

        when(packageRepository.deletePackageById(anyString())).thenReturn(deletedPackageModel);

        PackageDTO result = packageService.deletePackageById(packageDTO.getId());

        assertNotNull(result);
        assertEquals(packageDTO.getId(), result.getId());
        verify(packageRepository, times(1)).deletePackageById(anyString());
    }
}
