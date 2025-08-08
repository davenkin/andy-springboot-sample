package deviceet.sample.maintenance.domain.task;

import com.mongodb.client.result.DeleteResult;
import deviceet.sample.maintenance.domain.MaintenanceRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component
@RequiredArgsConstructor
public class DeleteAllMaintenanceRecordsUnderEquipmentTask {
    private final MongoTemplate mongoTemplate;

    public long run(String equipmentId) {
        Query query = query(where(MaintenanceRecord.Fields.equipmentId).is(equipmentId));
        DeleteResult result = mongoTemplate.remove(query, MaintenanceRecord.class);
        return result.getDeletedCount();
    }
}
