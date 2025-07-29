package deviceet.common.event;

import deviceet.common.model.AggregateRoot;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;

import static deviceet.common.utils.SnowflakeIdGenerator.newSnowflakeId;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;

@Getter
@FieldNameConstants
@NoArgsConstructor(access = PROTECTED)
public abstract class DomainEvent {
    private String id;
    private String arId;
    private String arTenantId;
    private DomainEventType type;
    private Instant raisedAt;
    private String raisedBy;

    protected DomainEvent(DomainEventType type, AggregateRoot ar) {
        requireNonNull(type, "Domain event type must not be null.");
        requireNonNull(ar, "Aggregate root ID must not be null.");

        this.id = newEventId();
        this.arId = ar.getId();
        this.arTenantId = ar.getTenantId();
        this.type = type;
        this.raisedAt = Instant.now();
    }

    public static String newEventId() {
        return "EVT" + newSnowflakeId();
    }

    public void raisedBy(String raisedBy) {
        this.raisedBy = raisedBy;
    }
}
