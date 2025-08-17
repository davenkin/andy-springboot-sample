package deviceet.business.test.controller;

import java.time.Instant;

public record QListedEquipment(String id,
                               String orgId,
                               String name,
                               EquipmentStatus status,
                               Instant createdAt,
                               String createdBy) {
}
