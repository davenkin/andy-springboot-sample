package deviceet.sample.maintenance.domain;

import java.util.Optional;

public interface MaintenanceRecordRepository {
    void save(MaintenanceRecord record);

    MaintenanceRecord byId(String id, String orgId);

    void delete(MaintenanceRecord record);

    Optional<MaintenanceRecord> latestFor(String equipmentId);
}
