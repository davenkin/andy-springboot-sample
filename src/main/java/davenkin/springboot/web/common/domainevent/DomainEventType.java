package davenkin.springboot.web.common.domainevent;

import davenkin.springboot.web.common.model.AggregateRootType;

import static davenkin.springboot.web.common.model.AggregateRootType.DEPARTMENT;
import static davenkin.springboot.web.common.model.AggregateRootType.USER;

public enum DomainEventType {
    USER_CREATED(USER),
    USER_NAME_UPDATED(USER),
    DEPARTMENT_CREATED(DEPARTMENT);

    private final AggregateRootType arType;

    DomainEventType(AggregateRootType arType) {
        this.arType = arType;
    }

    public AggregateRootType getArType() {
        return arType;
    }
}
