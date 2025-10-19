package com.company.andy.sample.equipment.query;

import com.company.andy.IntegrationTest;
import com.company.andy.common.model.operator.Operator;
import com.company.andy.common.util.PagedResponse;
import com.company.andy.sample.equipment.command.EquipmentCommandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.IntStream;

import static com.company.andy.RandomTestUtils.randomCreateEquipmentCommand;
import static com.company.andy.RandomTestUtils.randomUserOperator;
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

        EquipmentPagedQuery query = EquipmentPagedQuery.builder().pageSize(12).build();
        PagedResponse<QPagedEquipment> listedEquipments = equipmentQueryService.listEquipments(query, operator);

        assertEquals(12, listedEquipments.getContent().size());
    }
}
