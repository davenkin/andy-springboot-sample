package deviceet.sample.equipment;

import deviceet.common.utils.ResponseId;
import deviceet.sample.equipment.command.CreateEquipmentCommand;
import deviceet.sample.equipment.command.EquipmentCommandService;
import deviceet.sample.equipment.command.UpdateEquipmentNameCommand;
import deviceet.sample.equipment.query.EquipmentQueryService;
import deviceet.sample.equipment.query.ListEquipmentQuery;
import deviceet.sample.equipment.query.QListedEquipment;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static deviceet.TestUtils.TEST_PRINCIPAL;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/equipments")
public class EquipmentController {
    private final EquipmentCommandService equipmentCommandService;
    private final EquipmentQueryService equipmentQueryService;

    @PostMapping
    public ResponseId createEquipment(@RequestBody @Valid CreateEquipmentCommand command) {
        return new ResponseId(this.equipmentCommandService.createEquipment(command, TEST_PRINCIPAL));
    }

    @PutMapping(value = "/{equipmentId}/name")
    public void updateEquipmentName(@PathVariable("equipmentId") @NotBlank String equipmentId,
                                    @RequestBody @Valid UpdateEquipmentNameCommand updateEquipmentNameCommand) {
        this.equipmentCommandService.updateEquipmentName(equipmentId, updateEquipmentNameCommand, TEST_PRINCIPAL);
    }

    @PostMapping("/list")
    public Page<QListedEquipment> listEquipments(@RequestBody @Valid ListEquipmentQuery query,
                                                 @PageableDefault Pageable pageable) {
        return this.equipmentQueryService.listEquipments(query, pageable, TEST_PRINCIPAL);
    }

}
