package com.hiberus.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageModel {
    @NotNull
    private String id;
    private String riderId;
    private String previousWarehouse;
    @NotNull
    private String actualWarehouse;
    private String destinyWarehouse;
    @NotNull
    private Status status;

    public enum Status {
        IN_TRANSIT, AT_WAREHOUSE
    }
}

