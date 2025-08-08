package deviceet.sample.equipment.domain.task;

import deviceet.sample.equipment.domain.Equipment;
import deviceet.sample.maintenance.domain.MaintenanceRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNull;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Slf4j
@Component
@RequiredArgsConstructor
public class CountMaintenanceRecordsForEquipmentTask {
    private final MongoTemplate mongoTemplate;

    public void run(String equipmentId) {
        requireNonNull(equipmentId, "equipmentId should not be null.");

        Query query = Query.query(where(MaintenanceRecord.Fields.equipmentId).is(equipmentId));
        long count = mongoTemplate.count(query, MaintenanceRecord.class);

        Update update = new Update().set(Equipment.Fields.maintenanceRecordCount, count);
        mongoTemplate.updateFirst(query, update, MaintenanceRecord.class);
        log.info("Counted maintenance records under equipment[{}].", equipmentId);
    }
}
