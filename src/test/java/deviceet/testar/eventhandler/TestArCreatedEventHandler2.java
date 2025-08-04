package deviceet.testar.eventhandler;

import deviceet.common.event.consume.AbstractEventHandler;
import deviceet.testar.domain.event.TestArCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestArCreatedEventHandler2 extends AbstractEventHandler<TestArCreatedEvent> {
    @Override
    public void handle(TestArCreatedEvent event) {
        log.info("{} called for TestAr[{}].", this.getClass().getSimpleName(), event.getArId());
    }
}
