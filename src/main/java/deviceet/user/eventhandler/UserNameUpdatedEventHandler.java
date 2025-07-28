package deviceet.user.eventhandler;

import deviceet.common.event.consume.AbstractEventHandler;
import deviceet.user.domain.event.UserNameUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserNameUpdatedEventHandler extends AbstractEventHandler<UserNameUpdatedEvent> {

    @Override
    public void handle(UserNameUpdatedEvent event) {
        log.info("Handler called for user name updated[{}] created.", event.getNewName());
    }
}
