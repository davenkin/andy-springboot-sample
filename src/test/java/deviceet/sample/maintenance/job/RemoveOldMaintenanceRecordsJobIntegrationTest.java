package deviceet.sample.maintenance.job;

import deviceet.IntegrationTest;
import deviceet.common.model.AggregateRoot;
import deviceet.common.model.principal.Operator;
import deviceet.sample.equipment.command.CreateEquipmentCommand;
import deviceet.sample.equipment.command.EquipmentCommandService;
import deviceet.sample.maintenance.command.CreateMaintenanceRecordCommand;
import deviceet.sample.maintenance.command.MaintenanceRecordCommandService;
import deviceet.sample.maintenance.domain.MaintenanceRecord;
import deviceet.sample.maintenance.domain.MaintenanceRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.Instant;

import static deviceet.RandomTestUtils.*;
import static deviceet.common.util.Constants.MONGO_ID;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.data.mongodb.core.query.Criteria.where;

class RemoveOldMaintenanceRecordsJobIntegrationTest extends IntegrationTest {
    @Autowired
    private MaintenanceRecordCommandService maintenanceRecordCommandService;

    @Autowired
    private EquipmentCommandService equipmentCommandService;

    @Autowired
    private RemoveOldMaintenanceRecordsJob removeOldMaintenanceRecordsJob;

    @Autowired
    private MaintenanceRecordRepository maintenanceRecordRepository;

    @Test
    void should_remove_old_maintenance_records() {
        Operator operator = randomUserPrincipal();
        CreateEquipmentCommand createEquipmentCommand = randomCreateEquipmentCommand();
        String equipmentId = equipmentCommandService.createEquipment(createEquipmentCommand, operator);

        CreateMaintenanceRecordCommand createMaintenanceRecordCommand = randomCreateMaintenanceRecordCommand(equipmentId);
        String maintenanceRecordId = maintenanceRecordCommandService.createMaintenanceRecord(createMaintenanceRecordCommand, operator);
        String oldMaintenanceRecordId = maintenanceRecordCommandService.createMaintenanceRecord(createMaintenanceRecordCommand, operator);

        Query query = Query.query(where(MONGO_ID).is(oldMaintenanceRecordId));
        Update update = new Update().set(AggregateRoot.Fields.createdAt, Instant.now().minus(500, DAYS));
        mongoTemplate.updateFirst(query, update, MaintenanceRecord.class);

        removeOldMaintenanceRecordsJob.run();

        assertFalse(maintenanceRecordRepository.exists(oldMaintenanceRecordId));
        assertTrue(maintenanceRecordRepository.exists(maintenanceRecordId));
    }

}
