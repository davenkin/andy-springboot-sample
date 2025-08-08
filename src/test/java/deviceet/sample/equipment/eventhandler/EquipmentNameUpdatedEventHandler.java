package deviceet.sample.equipment.eventhandler;

import deviceet.common.event.consume.AbstractEventHandler;
import deviceet.sample.equipment.domain.EquipmentRepository;
import deviceet.sample.equipment.domain.event.EquipmentNameUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EquipmentNameUpdatedEventHandler extends AbstractEventHandler<EquipmentNameUpdatedEvent> {
    private final EquipmentRepository equipmentRepository;

    @Override
    public void handle(EquipmentNameUpdatedEvent event) {
        equipmentRepository.evictCachedEquipmentSummaries(event.getArOrgId());
        log.debug("Evicted equipment summaries cache for org[{}].", event.getArOrgId());

        //todo: 同步record的equipmentname
    }
}
