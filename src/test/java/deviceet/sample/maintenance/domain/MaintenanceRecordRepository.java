package deviceet.sample.maintenance.domain;

public interface MaintenanceRecordRepository {
    void save(MaintenanceRecord record);

    MaintenanceRecord byId(String id, String orgId);

    void delete(MaintenanceRecord record);
}
