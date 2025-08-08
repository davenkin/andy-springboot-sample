package deviceet.sample.maintenance;

import deviceet.common.model.Principal;
import deviceet.common.utils.ResponseId;
import deviceet.sample.maintenance.command.CreateMaintenanceRecordCommand;
import deviceet.sample.maintenance.command.MaintenanceRecordCommandService;
import deviceet.sample.maintenance.query.ListMaintenanceRecordsQuery;
import deviceet.sample.maintenance.query.MaintenanceRecordQueryService;
import deviceet.sample.maintenance.query.QDetailedMaintenanceRecord;
import deviceet.sample.maintenance.query.QListedMaintenanceRecord;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static deviceet.TestConfiguration.TEST_PRINCIPAL;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/maintenance-records")
public class MaintenanceRecordController {
    private final MaintenanceRecordCommandService maintenanceRecordCommandService;
    private final MaintenanceRecordQueryService maintenanceRecordQueryService;

    @PostMapping
    public ResponseId createMaintenanceRecord(@RequestBody @Valid CreateMaintenanceRecordCommand command) {
        // In real situations, principal is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Principal principal = TEST_PRINCIPAL;

        return new ResponseId(maintenanceRecordCommandService.createMaintenanceRecord(command, principal));
    }

    @DeleteMapping("/{maintenanceRecordId}")
    public void deleteMaintenanceRecord(@PathVariable("maintenanceRecordId") @NotBlank String maintenanceRecordId) {
        // In real situations, principal is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Principal principal = TEST_PRINCIPAL;

        this.maintenanceRecordCommandService.deleteMaintenanceRecord(maintenanceRecordId, principal);
    }

    @PostMapping("/list")
    public Page<QListedMaintenanceRecord> listMaintenanceRecords(@RequestBody @Valid ListMaintenanceRecordsQuery query,
                                                                 @PageableDefault Pageable pageable) {
        // In real situations, principal is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Principal principal = TEST_PRINCIPAL;

        return maintenanceRecordQueryService.listMaintenanceRecords(query, pageable, principal);
    }

    @GetMapping("/{maintenanceRecordId}")
    public QDetailedMaintenanceRecord getMaintenanceRecordDetail(@PathVariable("maintenanceRecordId") @NotBlank String maintenanceRecordId) {
        // In real situations, principal is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Principal principal = TEST_PRINCIPAL;

        return maintenanceRecordQueryService.getMaintenanceRecordDetail(maintenanceRecordId, principal);
    }

}
