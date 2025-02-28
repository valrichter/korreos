package com.hiberus.package_query.mapper;

import com.hiberus.model.PackageModel;
import com.hiberus.package_query.dto.PackageDTO;

public class PackageMapper {

    public static PackageModel toModel(PackageDTO dto) {
        if (dto == null) {
            return null;
        }

        return PackageModel.builder()
                .id(dto.getId())
                .riderId(dto.getRiderId())
                .previousWarehouse(dto.getPreviousWarehouse())
                .actualWarehouse(dto.getActualWarehouse())
                .destinyWarehouse(dto.getDestinyWarehouse())
                .status(PackageModel.Status.valueOf(dto.getStatus().name()))
                .build();
    }

    public static PackageDTO toDTO(PackageModel model) {
        if (model == null) {
            return null;
        }

        return PackageDTO.builder()
                .id(model.getId())
                .riderId(model.getRiderId())
                .previousWarehouse(model.getPreviousWarehouse())
                .actualWarehouse(model.getActualWarehouse())
                .destinyWarehouse(model.getDestinyWarehouse())
                .status(PackageModel.Status.valueOf(model.getStatus().name()))
                .build();
    }
}

