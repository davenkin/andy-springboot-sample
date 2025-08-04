package deviceet.business.testar.eventhandler;

import deviceet.business.testar.domain.event.TestArUpdatedEvent;
import deviceet.common.event.consume.AbstractEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestArUpdatedEventHandler extends AbstractEventHandler<TestArUpdatedEvent> {

    @Override
    public void handle(TestArUpdatedEvent event) {
        log.info("{} called for TestAr[{}].", this.getClass().getSimpleName(), event.getArId());
    }
}
