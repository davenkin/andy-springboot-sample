package deviceet.sample.maintenance.infrastructure;

import deviceet.common.infrastructure.AbstractMongoRepository;
import deviceet.sample.maintenance.domain.MaintenanceRecord;
import deviceet.sample.maintenance.domain.MaintenanceRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MongoMaintenanceRecordRepository extends AbstractMongoRepository<MaintenanceRecord> implements MaintenanceRecordRepository {
}
