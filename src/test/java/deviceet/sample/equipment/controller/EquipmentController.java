package deviceet.sample.equipment.controller;

import deviceet.common.model.principal.Principal;
import deviceet.common.util.ResponseId;
import deviceet.sample.equipment.command.CreateEquipmentCommand;
import deviceet.sample.equipment.command.EquipmentCommandService;
import deviceet.sample.equipment.command.UpdateEquipmentHolderCommand;
import deviceet.sample.equipment.command.UpdateEquipmentNameCommand;
import deviceet.sample.equipment.domain.EquipmentSummary;
import deviceet.sample.equipment.query.EquipmentQueryService;
import deviceet.sample.equipment.query.ListEquipmentQuery;
import deviceet.sample.equipment.query.QDetailedEquipment;
import deviceet.sample.equipment.query.QListedEquipment;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static deviceet.sample.SampleFixture.SAMPLE_USER_PRINCIPAL;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/equipments")
public class EquipmentController {
    private final EquipmentCommandService equipmentCommandService;
    private final EquipmentQueryService equipmentQueryService;

    @PostMapping
    public ResponseId createEquipment(@RequestBody @Valid CreateEquipmentCommand command) {
        // In real situations, principal is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Principal principal = SAMPLE_USER_PRINCIPAL;

        return new ResponseId(this.equipmentCommandService.createEquipment(command, principal));
    }

    @PutMapping("/{equipmentId}/name")
    public void updateEquipmentName(@PathVariable("equipmentId") @NotBlank String equipmentId,
                                    @RequestBody @Valid UpdateEquipmentNameCommand updateEquipmentNameCommand) {
        // In real situations, principal is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Principal principal = SAMPLE_USER_PRINCIPAL;

        this.equipmentCommandService.updateEquipmentName(equipmentId, updateEquipmentNameCommand, principal);
    }

    @PutMapping("/{equipmentId}/holder")
    public void updateEquipmentHolder(@PathVariable("equipmentId") @NotBlank String equipmentId,
                                      @RequestBody @Valid UpdateEquipmentHolderCommand command) {
        // In real situations, principal is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Principal principal = SAMPLE_USER_PRINCIPAL;

        this.equipmentCommandService.updateEquipmentHolder(equipmentId, command, principal);
    }

    @DeleteMapping("/{equipmentId}")
    public void deleteEquipment(@PathVariable("equipmentId") @NotBlank String equipmentId) {
        // In real situations, principal is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Principal principal = SAMPLE_USER_PRINCIPAL;

        this.equipmentCommandService.deleteEquipment(equipmentId, principal);
    }

    @PostMapping("/list")
    public Page<QListedEquipment> listEquipments(@RequestBody @Valid ListEquipmentQuery query,
                                                 @PageableDefault Pageable pageable) {
        // In real situations, principal is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Principal principal = SAMPLE_USER_PRINCIPAL;

        return this.equipmentQueryService.listEquipments(query, pageable, principal);
    }

    @GetMapping("/{equipmentId}")
    public QDetailedEquipment getEquipmentDetail(@PathVariable("equipmentId") @NotBlank String equipmentId) {
        // In real situations, principal is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Principal principal = SAMPLE_USER_PRINCIPAL;

        return this.equipmentQueryService.getEquipmentDetail(equipmentId, principal);
    }

    @GetMapping("/summaries")
    public List<EquipmentSummary> getAllEquipmentSummaries() {
        // In real situations, principal is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Principal principal = SAMPLE_USER_PRINCIPAL;

        return this.equipmentQueryService.getAllEquipmentSummaries(principal);
    }

}
