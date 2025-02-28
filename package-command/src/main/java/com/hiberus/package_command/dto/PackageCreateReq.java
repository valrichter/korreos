package com.hiberus.package_command.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageCreateReq {
    @NotNull
    private String id;
    @NotNull
    private String actualWarehouse;
}
