package deviceet.business.sampledevice.eventhandler;

import deviceet.business.sampledevice.domain.event.TestArNameUpdatedEvent;
import deviceet.common.event.consume.AbstractEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestArNameUpdatedEventHandler extends AbstractEventHandler<TestArNameUpdatedEvent> {
    @Override
    public void handle(TestArNameUpdatedEvent event) {
        log.info("{} called for TestAr[{}].", this.getClass().getSimpleName(), event.getArId());
    }
}
