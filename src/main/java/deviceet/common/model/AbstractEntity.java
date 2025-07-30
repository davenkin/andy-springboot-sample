package deviceet.common.model;

import deviceet.common.event.DomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static deviceet.common.utils.CommonUtils.requireNonBlank;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

// Base class for all entities
@Getter
@FieldNameConstants
// The no arg constructor is used by Jackson and Spring Data etc. to create objects
@NoArgsConstructor(access = PROTECTED)
public abstract class AbstractEntity {
    private String id;
    private String orgId;

    // Domain events are stored temporarily in the entity and are not persisted together with the entities as events will be stored in separately
    // @Transient is very important for not persisting events with the entity, otherwise we need to do this manually by ourselves
    @Transient
    private List<DomainEvent> events;

    private Instant createdAt;

    @Version
    @Getter(PRIVATE)
    private Long _version;

    protected AbstractEntity(String id, String orgId) {
        requireNonBlank(id, "id must not be blank.");
        requireNonBlank(orgId, "orgId must not be blank.");

        this.id = id;
        this.orgId = orgId;
        this.createdAt = Instant.now();
    }

    // raiseEvent() only stores events in entity temporarily, the events will then be persisted into DB by Repository within the same transaction of saving entities
    // The actual sending of events to messaging middleware is handled by DomainEventPublisher
    protected void raiseEvent(DomainEvent event) {
        requireNonNull(event.getType(), "Domain event's type must not be null.");
        requireNonBlank(event.getEntityId(), "Domain event's entityId must not be null.");

        events().add(event);
    }

    private List<DomainEvent> events() {
        if (events == null) {
            this.events = new ArrayList<>();
        }

        return events;
    }

    public void clearEvents() {
        this.events = null;
    }
}
