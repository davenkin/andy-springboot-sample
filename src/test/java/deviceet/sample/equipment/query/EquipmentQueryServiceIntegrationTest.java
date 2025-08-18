package deviceet.sample.equipment.query;

import deviceet.IntegrationTest;
import deviceet.common.model.operator.Operator;
import deviceet.common.util.PagedResponse;
import deviceet.sample.equipment.command.EquipmentCommandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.IntStream;

import static deviceet.RandomTestUtils.randomCreateEquipmentCommand;
import static deviceet.RandomTestUtils.randomUserOperator;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EquipmentQueryServiceIntegrationTest extends IntegrationTest {
    @Autowired
    private EquipmentQueryService equipmentQueryService;

    @Autowired
    private EquipmentCommandService equipmentCommandService;

    @Test
    void should_list_equipments() {
        Operator operator = randomUserOperator();
        IntStream.range(0, 20).forEach(i -> {
            equipmentCommandService.createEquipment(randomCreateEquipmentCommand(), operator);
        });

        ListEquipmentQuery query = ListEquipmentQuery.builder().pageSize(12).build();
        PagedResponse<QListedEquipment> listedEquipments = equipmentQueryService.listEquipments(query, operator);

        assertEquals(12, listedEquipments.getContent().size());
    }
}
