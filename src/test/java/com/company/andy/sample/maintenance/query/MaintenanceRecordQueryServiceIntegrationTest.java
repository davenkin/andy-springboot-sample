package com.company.andy.sample.maintenance.query;

import com.company.andy.IntegrationTest;
import com.company.andy.common.model.operator.Operator;
import com.company.andy.common.util.PagedResponse;
import com.company.andy.sample.equipment.command.CreateEquipmentCommand;
import com.company.andy.sample.equipment.command.EquipmentCommandService;
import com.company.andy.sample.maintenance.command.MaintenanceRecordCommandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.IntStream;

import static com.company.andy.RandomTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MaintenanceRecordQueryServiceIntegrationTest extends IntegrationTest {
    @Autowired
    private EquipmentCommandService equipmentCommandService;

    @Autowired
    private MaintenanceRecordQueryService maintenanceRecordQueryService;

    @Autowired
    private MaintenanceRecordCommandService maintenanceRecordCommandService;

    @Test
    void should_list_maintenance_records() {
        Operator operator = randomUserOperator();
        CreateEquipmentCommand createEquipmentCommand = randomCreateEquipmentCommand();
        String equipmentId = equipmentCommandService.createEquipment(createEquipmentCommand, operator);
        IntStream.range(0, 20).forEach(i -> {
            maintenanceRecordCommandService.createMaintenanceRecord(randomCreateMaintenanceRecordCommand(equipmentId), operator);
        });

        ListMaintenanceRecordsQuery query = ListMaintenanceRecordsQuery.builder().pageSize(12).build();
        PagedResponse<QListedMaintenanceRecord> records = maintenanceRecordQueryService.listMaintenanceRecords(query, operator);

        assertEquals(12, records.getContent().size());
    }
}
