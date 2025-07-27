package deviceet.common.event;

import deviceet.common.model.AggregateRootType;
import lombok.Getter;

import static deviceet.common.model.AggregateRootType.USER;

@Getter
public enum DomainEventType {
    USER_CREATED_EVENT(USER),
    USER_NAME_UPDATED_EVENT(USER),
    ;
    private final AggregateRootType arType;

    DomainEventType(AggregateRootType arType) {
        this.arType = arType;
    }
}
