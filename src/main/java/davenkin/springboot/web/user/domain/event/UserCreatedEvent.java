package davenkin.springboot.web.user.domain.event;

import davenkin.springboot.web.common.domainevent.DomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

import static davenkin.springboot.web.common.domainevent.DomainEventType.USER_CREATED;
import static lombok.AccessLevel.PRIVATE;

@Getter
@TypeAlias("USER_CREATED_EVENT")
@NoArgsConstructor(access = PRIVATE)
public class UserCreatedEvent extends DomainEvent {
    private String name;

    public UserCreatedEvent(String name, String userId) {
        super(USER_CREATED, userId);
        this.name = name;
    }
}
