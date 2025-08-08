package deviceet.sample.maintenance.query;

import deviceet.sample.equipment.domain.EquipmentStatus;
import lombok.Builder;

@Builder
public record ListMaintenanceRecordsQuery(String search, EquipmentStatus status) {
}

