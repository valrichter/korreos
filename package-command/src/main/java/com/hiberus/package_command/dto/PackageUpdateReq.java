package com.hiberus.package_command.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageUpdateReq {
    @NotNull
    private String riderId;
    private String previousWarehouse;
    @NotNull
    private String actualWarehouse;
    private String destinyWarehouse;
}
