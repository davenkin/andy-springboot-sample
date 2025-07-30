package deviceet.common.event;

import deviceet.common.model.AbstractEntity;
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
    private String entityId;
    private String entityOrgId;
    private DomainEventType type;
    private Instant raisedAt;
    private String raisedBy;

    protected DomainEvent(DomainEventType type, AbstractEntity entity) {
        requireNonNull(type, "type must not be null.");
        requireNonNull(entity, "entity must not be null.");

        this.id = newEventId();
        this.entityId = entity.getId();
        this.entityOrgId = entity.getOrgId();
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
