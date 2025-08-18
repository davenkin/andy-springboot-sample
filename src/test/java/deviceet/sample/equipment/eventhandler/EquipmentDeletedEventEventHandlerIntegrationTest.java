package deviceet.sample.equipment.eventhandler;

import deviceet.IntegrationTest;
import deviceet.common.model.principal.Operator;
import deviceet.sample.equipment.command.CreateEquipmentCommand;
import deviceet.sample.equipment.command.EquipmentCommandService;
import deviceet.sample.equipment.domain.event.EquipmentDeletedEvent;
import deviceet.sample.maintenance.command.CreateMaintenanceRecordCommand;
import deviceet.sample.maintenance.command.MaintenanceRecordCommandService;
import deviceet.sample.maintenance.domain.MaintenanceRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static deviceet.RandomTestUtils.*;
import static deviceet.common.event.DomainEventType.EQUIPMENT_DELETED_EVENT;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EquipmentDeletedEventEventHandlerIntegrationTest extends IntegrationTest {
    @Autowired
    private EquipmentDeletedEventEventHandler equipmentDeletedEventEventHandler;

    @Autowired
    private EquipmentCommandService equipmentCommandService;

    @Autowired
    private MaintenanceRecordCommandService maintenanceRecordCommandService;

    @Autowired
    private MaintenanceRecordRepository maintenanceRecordRepository;

    @Test
    void delete_equipment_should_also_delete_all_its_maintenance_records() {
        Operator operator = randomUserPrincipal();
        CreateEquipmentCommand createEquipmentCommand = randomCreateEquipmentCommand();
        String equipmentId = equipmentCommandService.createEquipment(createEquipmentCommand, operator);
        CreateMaintenanceRecordCommand createMaintenanceRecordCommand = randomCreateMaintenanceRecordCommand(equipmentId);
        String maintenanceRecordId = maintenanceRecordCommandService.createMaintenanceRecord(createMaintenanceRecordCommand, operator);
        assertTrue(maintenanceRecordRepository.exists(maintenanceRecordId));

        equipmentCommandService.deleteEquipment(equipmentId, operator);
        EquipmentDeletedEvent equipmentDeletedEvent = latestEventFor(equipmentId, EQUIPMENT_DELETED_EVENT, EquipmentDeletedEvent.class);

        equipmentDeletedEventEventHandler.handle(equipmentDeletedEvent);
        assertFalse(maintenanceRecordRepository.exists(maintenanceRecordId));
    }
}
