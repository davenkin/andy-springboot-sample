package deviceet.sample.equipment.query;

import deviceet.sample.equipment.domain.EquipmentStatus;

import java.time.Instant;

public record QListedEquipment(
        String id,
        String orgId,
        String name,
        EquipmentStatus status,
        Instant createdAt,
        String createdBy) {
}
