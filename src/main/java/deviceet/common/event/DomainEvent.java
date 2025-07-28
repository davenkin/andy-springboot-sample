package deviceet.common.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import deviceet.common.model.AggregateRoot;
import deviceet.common.model.AggregateRootType;
import deviceet.user.domain.event.UserCreatedEvent;
import deviceet.user.domain.event.UserNameUpdatedEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;

import static deviceet.common.utils.SnowflakeIdGenerator.newSnowflakeId;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = UserCreatedEvent.class, name = "USER_CREATED_EVENT"),
        @JsonSubTypes.Type(value = UserNameUpdatedEvent.class, name = "USER_NAME_UPDATED_EVENT"),
})

@Getter
@FieldNameConstants
@NoArgsConstructor(access = PROTECTED)
public abstract class DomainEvent {
    private String id;
    private String arId;
    private String arTenantId;
    private DomainEventType type;
    private AggregateRootType arType;
    private Instant raisedAt;
    private String raisedBy;

    protected DomainEvent(DomainEventType type, AggregateRoot ar) {
        requireNonNull(type, "Domain event type must not be null.");
        requireNonNull(ar, "Aggregate root ID must not be null.");

        this.id = newEventId();
        this.arId = ar.getId();
        this.arTenantId = ar.getTenantId();
        this.type = type;
        this.arType = ar.getType();
        this.raisedAt = Instant.now();
    }

    public static String newEventId() {
        return "EVT" + newSnowflakeId();
    }

    public void raisedBy(String raisedBy) {
        this.raisedBy = raisedBy;
    }
}
