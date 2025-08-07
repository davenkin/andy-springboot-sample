package deviceet.sample.equipment.eventhandler;

import deviceet.common.event.consume.AbstractEventHandler;
import deviceet.sample.equipment.domain.event.EquipmentCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EquipmentCreatedEventHandler2 extends AbstractEventHandler<EquipmentCreatedEvent> {
    @Override
    public void handle(EquipmentCreatedEvent event) {
        log.info("{} called for Equipment[{}].", this.getClass().getSimpleName(), event.getArId());
    }
}
