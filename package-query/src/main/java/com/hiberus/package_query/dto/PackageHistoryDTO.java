package com.hiberus.package_query.dto;

import com.hiberus.model.PackageModel;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageHistoryDTO {
    private String packageId;
    private List<WarehouseHistoryDTO> history;
    private PackageModel.Status status;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class WarehouseHistoryDTO {
        private String warehouseName;
        private String riderName;
    }
}