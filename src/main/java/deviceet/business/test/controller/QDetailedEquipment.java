package deviceet.business.test.controller;

import java.time.Instant;

public record QDetailedEquipment(String id,
                                 String orgId,
                                 String name,
                                 Instant createdAt,
                                 String createdBy) {
}
