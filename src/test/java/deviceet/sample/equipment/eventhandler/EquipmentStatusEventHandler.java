package deviceet.sample.equipment.eventhandler;

import deviceet.common.event.consume.AbstractEventHandler;
import deviceet.sample.equipment.domain.EquipmentRepository;
import deviceet.sample.equipment.domain.event.EquipmentStatusUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EquipmentStatusEventHandler extends AbstractEventHandler<EquipmentStatusUpdatedEvent> {
    private final EquipmentRepository equipmentRepository;

    @Override
    public void handle(EquipmentStatusUpdatedEvent event) {
        equipmentRepository.evictCachedEquipmentSummaries(event.getArOrgId());
    }
}
