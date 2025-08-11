package deviceet.sample.equipment.eventhandler;

import deviceet.common.event.consume.AbstractEventHandler;
import deviceet.common.util.ExceptionSwallowRunner;
import deviceet.sample.equipment.domain.EquipmentRepository;
import deviceet.sample.equipment.domain.event.EquipmentNameUpdatedEvent;
import deviceet.sample.equipment.domain.task.SyncEquipmentNameToMaintenanceRecordsTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EquipmentNameUpdatedEventHandler extends AbstractEventHandler<EquipmentNameUpdatedEvent> {
    private final EquipmentRepository equipmentRepository;
    private final SyncEquipmentNameToMaintenanceRecordsTask syncEquipmentNameToMaintenanceRecordsTask;

    @Override
    public void handle(EquipmentNameUpdatedEvent event) {
        ExceptionSwallowRunner.run(() -> equipmentRepository.evictCachedEquipmentSummaries(event.getArOrgId()));
        ExceptionSwallowRunner.run(() -> syncEquipmentNameToMaintenanceRecordsTask.run(event.getEquipmentId()));
    }
}
