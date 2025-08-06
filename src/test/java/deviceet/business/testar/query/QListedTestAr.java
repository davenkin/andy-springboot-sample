package deviceet.business.testar.query;

import java.time.Instant;

public record QListedTestAr(
        String id,
        String orgId,
        String name,
        Instant createdAt,
        String createdBy) {
}
