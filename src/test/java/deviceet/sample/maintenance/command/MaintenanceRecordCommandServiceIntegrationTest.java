package deviceet.sample.maintenance.command;

import deviceet.IntegrationTest;
import deviceet.common.model.principal.Operator;
import deviceet.sample.equipment.command.CreateEquipmentCommand;
import deviceet.sample.equipment.command.EquipmentCommandService;
import deviceet.sample.maintenance.domain.MaintenanceRecord;
import deviceet.sample.maintenance.domain.MaintenanceRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static deviceet.RandomTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MaintenanceRecordCommandServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private MaintenanceRecordCommandService maintenanceRecordCommandService;

    @Autowired
    private EquipmentCommandService equipmentCommandService;

    @Autowired
    private MaintenanceRecordRepository maintenanceRecordRepository;

    @Test
    void should_create_maintenance_record() {
        Operator operator = randomUserPrincipal();
        CreateEquipmentCommand createEquipmentCommand = randomCreateEquipmentCommand();
        String equipmentId = equipmentCommandService.createEquipment(createEquipmentCommand, operator);

        CreateMaintenanceRecordCommand createMaintenanceRecordCommand = randomCreateMaintenanceRecordCommand(equipmentId);
        String maintenanceRecordId = maintenanceRecordCommandService.createMaintenanceRecord(createMaintenanceRecordCommand, operator);

        MaintenanceRecord maintenanceRecord = maintenanceRecordRepository.byId(maintenanceRecordId);
        assertEquals(equipmentId, maintenanceRecord.getEquipmentId());
    }
}
