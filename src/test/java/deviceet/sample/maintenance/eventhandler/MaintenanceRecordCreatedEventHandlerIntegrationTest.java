package deviceet.sample.maintenance.eventhandler;

import deviceet.IntegrationTest;
import deviceet.common.model.principal.Principal;
import deviceet.sample.equipment.command.CreateEquipmentCommand;
import deviceet.sample.equipment.command.EquipmentCommandService;
import deviceet.sample.equipment.domain.Equipment;
import deviceet.sample.equipment.domain.EquipmentRepository;
import deviceet.sample.maintenance.command.CreateMaintenanceRecordCommand;
import deviceet.sample.maintenance.command.MaintenanceRecordCommandService;
import deviceet.sample.maintenance.domain.event.MaintenanceRecordCreatedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static deviceet.RandomTestUtils.*;
import static deviceet.common.event.DomainEventType.MAINTENANCE_RECORD_CREATED_EVENT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MaintenanceRecordCreatedEventHandlerIntegrationTest extends IntegrationTest {
    @Autowired
    private EquipmentCommandService equipmentCommandService;

    @Autowired
    private MaintenanceRecordCommandService maintenanceRecordCommandService;

    @Autowired
    private MaintenanceRecordCreatedEventHandler maintenanceRecordCreatedEventHandler;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Test
    void should_count_maintenance_records_for_equipment() {
        Principal principal = randomUserPrincipal();
        CreateEquipmentCommand createEquipmentCommand = randomCreateEquipmentCommand();
        String equipmentId = equipmentCommandService.createEquipment(createEquipmentCommand, principal);
        CreateMaintenanceRecordCommand createMaintenanceRecordCommand = randomCreateMaintenanceRecordCommand(equipmentId);
        String maintenanceRecordId = maintenanceRecordCommandService.createMaintenanceRecord(createMaintenanceRecordCommand, principal);
        String maintenanceRecordId2 = maintenanceRecordCommandService.createMaintenanceRecord(createMaintenanceRecordCommand, principal);
        MaintenanceRecordCreatedEvent createdEvent = latestEventFor(maintenanceRecordId, MAINTENANCE_RECORD_CREATED_EVENT, MaintenanceRecordCreatedEvent.class);
        MaintenanceRecordCreatedEvent createdEvent2 = latestEventFor(maintenanceRecordId2, MAINTENANCE_RECORD_CREATED_EVENT, MaintenanceRecordCreatedEvent.class);

        maintenanceRecordCreatedEventHandler.handle(createdEvent);
        maintenanceRecordCreatedEventHandler.handle(createdEvent2);

        assertEquals(2, equipmentRepository.byId(equipmentId).getMaintenanceRecordCount());
    }

    @Test
    void should_update_status_for_equipment_using_maintenance_record_status() {
        Principal principal = randomUserPrincipal();
        CreateEquipmentCommand createEquipmentCommand = randomCreateEquipmentCommand();
        String equipmentId = equipmentCommandService.createEquipment(createEquipmentCommand, principal);
        CreateMaintenanceRecordCommand createMaintenanceRecordCommand = randomCreateMaintenanceRecordCommand(equipmentId);
        String maintenanceRecordId = maintenanceRecordCommandService.createMaintenanceRecord(createMaintenanceRecordCommand, principal);

        MaintenanceRecordCreatedEvent createdEvent = latestEventFor(maintenanceRecordId, MAINTENANCE_RECORD_CREATED_EVENT, MaintenanceRecordCreatedEvent.class);
        maintenanceRecordCreatedEventHandler.handle(createdEvent);

        Equipment equipment = equipmentRepository.byId(equipmentId);
        assertEquals(createMaintenanceRecordCommand.status(), equipment.getStatus());
    }

}
