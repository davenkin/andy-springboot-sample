package deviceet.sample.equipment.eventhandler;

import deviceet.common.event.consume.AbstractEventHandler;
import deviceet.sample.equipment.domain.event.EquipmentNameUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EquipmentNameUpdatedEventHandler extends AbstractEventHandler<EquipmentNameUpdatedEvent> {
    @Override
    public void handle(EquipmentNameUpdatedEvent event) {
        log.info("{} called for Equipment[{}].", this.getClass().getSimpleName(), event.getArId());
        // todo: evict all equipment cache
    }
}
