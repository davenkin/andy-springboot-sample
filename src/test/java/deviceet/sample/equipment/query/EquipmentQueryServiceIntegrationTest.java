package deviceet.sample.equipment.query;

import deviceet.IntegrationTest;
import deviceet.common.model.Principal;
import deviceet.sample.equipment.command.CreateEquipmentCommand;
import deviceet.sample.equipment.command.EquipmentCommandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.stream.IntStream;

import static deviceet.RandomTestUtils.randomEquipmentName;
import static deviceet.RandomTestUtils.randomPrincipal;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EquipmentQueryServiceIntegrationTest extends IntegrationTest {
    @Autowired
    private EquipmentQueryService equipmentQueryService;

    @Autowired
    private EquipmentCommandService equipmentCommandService;

    @Test
    void shouldListEquipments() {
        Principal principal = randomPrincipal();
        IntStream.range(0, 20).forEach(i -> {
            CreateEquipmentCommand createEquipmentCommand = new CreateEquipmentCommand(randomEquipmentName());
            equipmentCommandService.createEquipment(createEquipmentCommand, principal);
        });
        ListEquipmentQuery query = ListEquipmentQuery.builder().build();

        Page<QListedEquipment> listedEquipments = equipmentQueryService.listEquipments(query, PageRequest.of(0, 12), principal);

        assertEquals(12, listedEquipments.getContent().size());
    }
}
