package deviceet.sample.equipment.query;

import deviceet.IntegrationTest;
import deviceet.common.model.principal.Operator;
import deviceet.sample.equipment.command.EquipmentCommandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.stream.IntStream;

import static deviceet.RandomTestUtils.randomCreateEquipmentCommand;
import static deviceet.RandomTestUtils.randomUserPrincipal;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EquipmentQueryServiceIntegrationTest extends IntegrationTest {
    @Autowired
    private EquipmentQueryService equipmentQueryService;

    @Autowired
    private EquipmentCommandService equipmentCommandService;

    @Test
    void should_list_equipments() {
        Operator operator = randomUserPrincipal();
        IntStream.range(0, 20).forEach(i -> {
            equipmentCommandService.createEquipment(randomCreateEquipmentCommand(), operator);
        });

        ListEquipmentQuery query = ListEquipmentQuery.builder().build();
        Page<QListedEquipment> listedEquipments = equipmentQueryService.listEquipments(query, PageRequest.of(0, 12), operator);

        assertEquals(12, listedEquipments.getContent().size());
    }
}
