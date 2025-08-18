package deviceet.sample.maintenance.controller;

import deviceet.common.model.principal.Operator;
import deviceet.common.util.ResponseId;
import deviceet.sample.maintenance.command.CreateMaintenanceRecordCommand;
import deviceet.sample.maintenance.command.MaintenanceRecordCommandService;
import deviceet.sample.maintenance.query.ListMaintenanceRecordsQuery;
import deviceet.sample.maintenance.query.MaintenanceRecordQueryService;
import deviceet.sample.maintenance.query.QDetailedMaintenanceRecord;
import deviceet.sample.maintenance.query.QListedMaintenanceRecord;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static deviceet.sample.SampleFixture.SAMPLE_USER_OPERATOR;
import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "MaintenanceRecordController", description = "Equipments' maintenance record APIs")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/maintenance-records")
public class MaintenanceRecordController {
    private final MaintenanceRecordCommandService maintenanceRecordCommandService;
    private final MaintenanceRecordQueryService maintenanceRecordQueryService;

    @Operation(summary = "Create a maintenance record")
    @ResponseStatus(CREATED)
    @PostMapping
    public ResponseId createMaintenanceRecord(@RequestBody @Valid CreateMaintenanceRecordCommand command) {
        // In real situations, principal is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Operator operator = SAMPLE_USER_OPERATOR;

        return new ResponseId(maintenanceRecordCommandService.createMaintenanceRecord(command, operator));
    }

    @Operation(summary = "Delete a maintenance record")
    @DeleteMapping("/{maintenanceRecordId}")
    public void deleteMaintenanceRecord(@PathVariable("maintenanceRecordId") @NotBlank String maintenanceRecordId) {
        // In real situations, principal is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Operator operator = SAMPLE_USER_OPERATOR;

        this.maintenanceRecordCommandService.deleteMaintenanceRecord(maintenanceRecordId, operator);
    }

    @Operation(summary = "Query maintenance records")
    @PostMapping("/list")
    public Page<QListedMaintenanceRecord> listMaintenanceRecords(@RequestBody @Valid ListMaintenanceRecordsQuery query,
                                                                 @PageableDefault Pageable pageable) {
        // In real situations, principal is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Operator operator = SAMPLE_USER_OPERATOR;

        return maintenanceRecordQueryService.listMaintenanceRecords(query, pageable, operator);
    }

    @Operation(summary = "Get maintenance record detail")
    @GetMapping("/{maintenanceRecordId}")
    public QDetailedMaintenanceRecord getMaintenanceRecordDetail(@PathVariable("maintenanceRecordId") @NotBlank String maintenanceRecordId) {
        // In real situations, principal is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Operator operator = SAMPLE_USER_OPERATOR;

        return maintenanceRecordQueryService.getMaintenanceRecordDetail(maintenanceRecordId, operator);
    }

}
