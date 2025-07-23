package davenkin.springboot.web.department.eventhandler;

import davenkin.springboot.web.common.domainevent.consume.AbstractDomainEventHandler;
import davenkin.springboot.web.department.domain.event.DepartmentCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class DepartmentCreatedEventHandler extends AbstractDomainEventHandler<DepartmentCreatedEvent> {
    @Override
    protected void doHandle(DepartmentCreatedEvent domainEvent) {
        log.info("Send email to admin after department[{}] created.", domainEvent.getName());
    }

}
