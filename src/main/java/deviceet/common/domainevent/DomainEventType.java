package deviceet.common.domainevent;

import deviceet.common.model.AggregateRootType;
import lombok.Getter;

import static deviceet.common.model.AggregateRootType.USER;

@Getter
public enum DomainEventType {
    USER_CREATED(USER),
    USER_NAME_UPDATED(USER),
    ;
    private final AggregateRootType arType;

    DomainEventType(AggregateRootType arType) {
        this.arType = arType;
    }
}
