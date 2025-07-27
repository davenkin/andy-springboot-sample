package deviceet.user.domain.event;

import deviceet.common.event.DomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

import static deviceet.common.event.DomainEventType.USER_NAME_UPDATED_EVENT;
import static lombok.AccessLevel.PRIVATE;

@Getter
@TypeAlias("USER_NAME_UPDATED_EVENT")
@NoArgsConstructor(access = PRIVATE)
public class UserNameUpdatedEvent extends DomainEvent {
    private String oldName;
    private String newName;

    public UserNameUpdatedEvent(String userId, String oldName, String newName) {
        super(USER_NAME_UPDATED_EVENT, userId);
        this.oldName = oldName;
        this.newName = newName;
    }
}
