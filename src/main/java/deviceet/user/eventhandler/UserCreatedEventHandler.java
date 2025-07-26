package deviceet.user.eventhandler;

import deviceet.common.event.consume.AbstractEventHandler;
import deviceet.user.domain.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCreatedEventHandler extends AbstractEventHandler<UserCreatedEvent> {

    @Override
    public boolean isTransactional() {
        return true;
    }

    @Override
    public void handle(UserCreatedEvent event) {
        log.info("Send email to admin after user[{}] created.", event.getName());
    }
}
