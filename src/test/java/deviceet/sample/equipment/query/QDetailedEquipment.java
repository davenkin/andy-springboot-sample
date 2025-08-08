package deviceet.sample.equipment.query;

import deviceet.sample.equipment.domain.EquipmentStatus;
import lombok.Builder;

import java.time.Instant;

@Builder
public record QDetailedEquipment(
        String id,
        String orgId,
        String name,
        EquipmentStatus status,
        Instant createdAt,
        String createdBy) {
}
