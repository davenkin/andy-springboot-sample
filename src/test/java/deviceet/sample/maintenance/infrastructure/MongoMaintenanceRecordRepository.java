package deviceet.sample.maintenance.infrastructure;

import deviceet.common.infrastructure.AbstractMongoRepository;
import deviceet.sample.maintenance.domain.MaintenanceRecord;
import deviceet.sample.maintenance.domain.MaintenanceRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static deviceet.common.utils.CommonUtils.requireNonBlank;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@RequiredArgsConstructor
public class MongoMaintenanceRecordRepository extends AbstractMongoRepository<MaintenanceRecord> implements MaintenanceRecordRepository {
    @Override
    public Optional<MaintenanceRecord> latestFor(String equipmentId) {
        requireNonBlank(equipmentId, "equipmentId must not be blank.");

        Query query = Query.query(where(MaintenanceRecord.Fields.equipmentId).is(equipmentId));
        return Optional.ofNullable(mongoTemplate.findOne(query, MaintenanceRecord.class));
    }
}
