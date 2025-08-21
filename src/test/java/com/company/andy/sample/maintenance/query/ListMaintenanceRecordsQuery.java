package com.company.andy.sample.maintenance.query;

import com.company.andy.common.util.PageableRequest;
import com.company.andy.sample.equipment.domain.EquipmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PRIVATE;

@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = PRIVATE)
public class ListMaintenanceRecordsQuery extends PageableRequest {
    @Schema(description = "Search text")
    @Max(50)
    private String search;

    @Schema(description = "Equipment status to query")
    private EquipmentStatus status;
}

