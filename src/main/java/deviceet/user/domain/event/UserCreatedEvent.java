package deviceet.user.domain.event;

import deviceet.common.event.DomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

import static deviceet.common.event.DomainEventType.USER_CREATED;
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
