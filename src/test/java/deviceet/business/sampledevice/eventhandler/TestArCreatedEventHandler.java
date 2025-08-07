package deviceet.business.sampledevice.eventhandler;

import deviceet.business.sampledevice.domain.event.TestArCreatedEvent;
import deviceet.common.event.consume.AbstractEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestArCreatedEventHandler extends AbstractEventHandler<TestArCreatedEvent> {
    @Override
    public void handle(TestArCreatedEvent event) {
        log.info("{} called for TestAr[{}].", this.getClass().getSimpleName(), event.getArId());
    }
}
