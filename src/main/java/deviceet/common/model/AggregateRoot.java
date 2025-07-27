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

// Base class for all aggregate roots
@Getter
@FieldNameConstants
// The no arg constructor is used by Jackson and Spring Data etc. to create objects
@NoArgsConstructor(access = PROTECTED)
public abstract class AggregateRoot {
    private String id;
    private String tenantId;
    private AggregateRootType type;

    // Domain events are stored temporarily in the aggregate root and are not persisted together with the aggregate roots as events will be stored in separately
    // @Transient is very important for not persisting events with the aggregate root, otherwise we need to do this manually by ourselves
    @Transient
    private List<DomainEvent> events;

    private Instant createdAt;

    @Version
    @Getter(PRIVATE)
    private Long _version;

    protected AggregateRoot(String id, String tenantId, AggregateRootType type) {
        requireNonBlank(id, "ID must not be blank.");
        requireNonBlank(tenantId, "Tenant ID must not be blank.");
        requireNonNull(type, "Type must not be null.");

        this.id = id;
        this.tenantId = tenantId;
        this.type = type;
        this.createdAt = Instant.now();
    }

    // raiseEvent() only stores events in aggregate root temporarily, the events will then be persisted into DB by Repository within the same transaction of saving aggregate roots
    // The actual sending of events to messaging middleware is handled by DomainEventPublisher
    protected void raiseEvent(DomainEvent event) {
        requireNonNull(event.getType(), "Domain event type must not be null.");
        requireNonBlank(event.getArId(), "Domain event aggregate root ID must not be null.");

        events().add(event);
    }

    private List<DomainEvent> events() {
        if (events == null) {
            this.events = new ArrayList<>();
        }

        return events;
    }
}
