package deviceet.sample.maintenance.query;

import deviceet.IntegrationTest;
import deviceet.common.model.principal.Operator;
import deviceet.sample.equipment.command.CreateEquipmentCommand;
import deviceet.sample.equipment.command.EquipmentCommandService;
import deviceet.sample.maintenance.command.MaintenanceRecordCommandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.stream.IntStream;

import static deviceet.RandomTestUtils.*;
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
        Operator operator = randomUserPrincipal();
        CreateEquipmentCommand createEquipmentCommand = randomCreateEquipmentCommand();
        String equipmentId = equipmentCommandService.createEquipment(createEquipmentCommand, operator);
        IntStream.range(0, 20).forEach(i -> {
            maintenanceRecordCommandService.createMaintenanceRecord(randomCreateMaintenanceRecordCommand(equipmentId), operator);
        });

        ListMaintenanceRecordsQuery query = ListMaintenanceRecordsQuery.builder().build();
        Page<QListedMaintenanceRecord> records = maintenanceRecordQueryService.listMaintenanceRecords(query, PageRequest.of(0, 12), operator);

        assertEquals(12, records.getContent().size());
    }
}
