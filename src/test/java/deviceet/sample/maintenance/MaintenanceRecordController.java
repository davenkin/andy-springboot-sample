package deviceet.sample.maintenance;

import deviceet.common.model.Principal;
import deviceet.common.utils.ResponseId;
import deviceet.sample.equipment.command.CreateEquipmentCommand;
import deviceet.sample.equipment.command.EquipmentCommandService;
import deviceet.sample.equipment.query.EquipmentQueryService;
import deviceet.sample.equipment.query.ListEquipmentQuery;
import deviceet.sample.equipment.query.QListedEquipment;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static deviceet.TestConfiguration.TEST_PRINCIPAL;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/maintenance-records")
public class MaintenanceRecordController {
    private final EquipmentCommandService equipmentCommandService;
    private final EquipmentQueryService equipmentQueryService;

    @PostMapping
    public ResponseId createEquipment(@RequestBody @Valid CreateEquipmentCommand command) {
        // In real situations, principal is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Principal principal = TEST_PRINCIPAL;
        return new ResponseId(this.equipmentCommandService.createEquipment(command, principal));
    }

    @PostMapping("/list")
    public Page<QListedEquipment> listEquipments(@RequestBody @Valid ListEquipmentQuery query,
                                                 @PageableDefault Pageable pageable) {
        // In real situations, principal is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Principal principal = TEST_PRINCIPAL;
        return this.equipmentQueryService.listEquipments(query, pageable, principal);
    }

}
