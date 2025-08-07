package deviceet.sample.equipment.query;

import java.time.Instant;

public record QListedEquipment(
        String id,
        String orgId,
        String name,
        Instant createdAt,
        String createdBy) {
}
