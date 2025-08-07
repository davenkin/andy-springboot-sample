package deviceet.sample.equipment.query;

import deviceet.sample.maintenance.MaintenanceStatus;

import java.time.Instant;

public record QListedEquipment(
        String id,
        String orgId,
        String name,
        MaintenanceStatus status,
        Instant createdAt,
        String createdBy) {
}
