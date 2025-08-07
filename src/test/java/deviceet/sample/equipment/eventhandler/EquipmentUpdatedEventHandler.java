package deviceet.sample.equipment.eventhandler;

import deviceet.common.event.consume.AbstractEventHandler;
import deviceet.sample.equipment.domain.event.EquipmentUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EquipmentUpdatedEventHandler extends AbstractEventHandler<EquipmentUpdatedEvent> {

    @Override
    public void handle(EquipmentUpdatedEvent event) {
        log.info("{} called for Equipment[{}].", this.getClass().getSimpleName(), event.getArId());
    }
}
