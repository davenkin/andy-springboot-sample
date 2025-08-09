package deviceet.common.model;

import deviceet.common.event.DomainEvent;
import deviceet.common.model.principal.Principal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static deviceet.common.util.CommonUtils.requireNonBlank;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

// Base class for all entities
@Getter
@FieldNameConstants
// The no arg constructor is used by Jackson and Spring Data etc. to create objects
@NoArgsConstructor(access = PROTECTED)
public abstract class AggregateRoot {
    private String id;
    private String orgId;

    // Domain events are stored temporarily in the aggregate root and are not persisted together with the entities as events will be stored in separately
    // @Transient is very important for not persisting events with the aggregate root, otherwise we need to do this manually by ourselves
    @Transient
    private List<DomainEvent> events;
    private Instant createdAt;
    private String createdBy;
    private Instant modifiedAt;
    private String modifiedBy;

    @Version
    @Getter(PRIVATE)
    private Long _version;

    protected AggregateRoot(String id, String orgId, Principal principal) {
        requireNonBlank(id, "id must not be blank.");
        requireNonBlank(orgId, "orgId must not be blank.");
        requireNonNull(principal, "principal must not be null.");

        this.id = id;
        this.orgId = orgId;
        this.createdAt = Instant.now();
        this.createdBy = principal.getId();
    }

    protected AggregateRoot(String id, Principal principal) {
        requireNonBlank(id, "id must not be blank.");
        requireNonNull(principal, "principal must not be null.");

        this.id = id;
        this.orgId = principal.getOrgId();
        this.createdAt = Instant.now();
        this.createdBy = principal.getId();
    }

    // raiseEvent() only stores events in aggregate root temporarily, the events will then be persisted into DB by Repository within the same transaction of saving entities
    // The actual sending of events to messaging middleware is handled by DomainEventPublisher
    protected final void raiseEvent(DomainEvent event) {
        requireNonNull(event, "event must not be null.");
        requireNonNull(event.getType(), "event's type must not be null.");
        requireNonBlank(event.getArId(), "event's arId must not be null.");

        events().add(event);
    }

    private List<DomainEvent> events() {
        if (events == null) {
            this.events = new ArrayList<>();
        }

        return events;
    }

    public final void clearEvents() {
        this.events = null;
    }

    public void onDelete() {
    }

    public void onModify(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        this.modifiedAt = Instant.now();
    }
}
