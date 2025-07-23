package davenkin.springboot.web.department.domain.event;

import davenkin.springboot.web.common.domainevent.DomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

import static davenkin.springboot.web.common.domainevent.DomainEventType.DEPARTMENT_CREATED;
import static lombok.AccessLevel.PRIVATE;

@Getter
@TypeAlias("DEPARTMENT_CREATED_EVENT")
@NoArgsConstructor(access = PRIVATE)
public class DepartmentCreatedEvent extends DomainEvent {
    private String name;

    public DepartmentCreatedEvent(String name, String departmentId) {
        super(DEPARTMENT_CREATED, departmentId);
        this.name = name;
    }
}
