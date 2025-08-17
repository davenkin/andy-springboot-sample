package deviceet.business.test.controller;

import deviceet.common.util.ResponseId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "TestController", description = "Test controller")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/equipments1")
public class TestController {

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseId createEquipment(@RequestBody @Valid CreateEquipmentCommand command) {
        return new ResponseId("abc");
    }

    @PutMapping("/{equipmentId}/name")
    public void updateEquipmentName(@PathVariable("equipmentId") @NotBlank
                                    @Parameter(description = "Equipment ID")

                                    String equipmentId,
                                    @RequestBody @Valid UpdateEquipmentNameCommand updateEquipmentNameCommand) {
    }


    @DeleteMapping("/{equipmentId}")
    public void deleteEquipment(@PathVariable("equipmentId") @NotBlank String equipmentId) {

    }

    @Operation(description = "Get list")
    @PostMapping("/list")
    public Page<QListedEquipment> listEquipments(@RequestBody
                                                 @Valid
                                                 @Parameter(description = "Query request for ")
                                                 AbcPageableRequest query) {
        return null;
    }

    @GetMapping("/{equipmentId}")
    public QDetailedEquipment getEquipmentDetail(@PathVariable("equipmentId") @NotBlank String equipmentId) {
        return null;
    }

}
