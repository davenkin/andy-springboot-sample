package deviceet.sample.equipment.domain;

import deviceet.sample.maintenance.MaintenanceStatus;
import lombok.Builder;

@Builder
public record EquipmentReference(String id,
                                 String name,
                                 String orgId,
                                 MaintenanceStatus status) {
}
