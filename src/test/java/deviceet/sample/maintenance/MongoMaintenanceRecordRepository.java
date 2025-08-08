package deviceet.sample.maintenance;

import deviceet.common.infrastructure.AbstractMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MongoMaintenanceRecordRepository extends AbstractMongoRepository<MaintenanceRecord> implements MaintenanceRecordRepository {
}
