package com.hiberus.package_query.dto;

import com.hiberus.model.PackageModel;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageDTO {
    private String id;
    private String riderId;
    private String previousWarehouse;
    private String actualWarehouse;
    private String destinyWarehouse;
    private PackageModel.Status status;
}
