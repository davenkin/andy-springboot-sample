package deviceet.sample.maintenance.query;

import deviceet.common.model.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MaintenanceRecordQueryService {
    private final MongoTemplate mongoTemplate;

    public Page<QListedMaintenanceRecord> listMaintenanceRecords(ListMaintenanceRecordsQuery listMaintenanceRecordsQuery,
                                                                 Pageable pageable,
                                                                 Principal principal) {
        return null;
    }

    public QDetailedMaintenanceRecord getMaintenanceRecordDetail(String maintenanceRecordId, Principal principal) {
        return null;
    }
}
