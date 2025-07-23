package davenkin.springboot.web.user.eventhandler;

import davenkin.springboot.web.common.domainevent.consume.AbstractTransactionalDomainEventHandler;
import davenkin.springboot.web.department.domain.DepartmentRepository;
import davenkin.springboot.web.user.domain.event.UserNameUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/*
 * We are updating DB so need to use AbstractTransactionalDomainEventHandler.
 * Here we use the name directly from the domain event,
 * if events run our of order it might result in wrong creator names in department,
 * so isIdempotent() should return false
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class NonIdempotentUserNameUpdatedEventHandler extends AbstractTransactionalDomainEventHandler<UserNameUpdatedEvent> {
    private final DepartmentRepository departmentRepository;

    // Update creator name for all departments created by this user
    @Override
    protected void doHandle(UserNameUpdatedEvent domainEvent) {
        this.departmentRepository.updateAllDepartmentsCreatorName(domainEvent.getArId(), domainEvent.getNewName());
    }

    @Override
    public boolean isIdempotent() {
        return false;
    }
}
