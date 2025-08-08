package deviceet.sample.equipment.command;

import deviceet.IntegrationTest;
import deviceet.common.model.Principal;
import deviceet.sample.equipment.domain.Equipment;
import deviceet.sample.equipment.domain.EquipmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static deviceet.TestUtils.randomEquipmentName;
import static deviceet.TestUtils.randomPrincipal;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EquipmentCommandServiceIntegrationTest extends IntegrationTest {
    @Autowired
    private EquipmentCommandService equipmentCommandService;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Test
    void shouldCreateEquipment() {
        CreateEquipmentCommand createEquipmentCommand = CreateEquipmentCommand.builder()
                .name(randomEquipmentName())
                .build();
        Principal principal = randomPrincipal();

        String equipmentId = equipmentCommandService.createEquipment(createEquipmentCommand, principal);

        Equipment equipment = equipmentRepository.byId(equipmentId);
        assertEquals(createEquipmentCommand.name(), equipment.getName());
        assertEquals(principal.getOrgId(), equipment.getOrgId());
    }
}
