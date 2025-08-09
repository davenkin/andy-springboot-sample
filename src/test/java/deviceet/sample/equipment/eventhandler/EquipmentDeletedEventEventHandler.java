package deviceet.sample.equipment.eventhandler;

import deviceet.common.event.consume.AbstractEventHandler;
import deviceet.common.util.TaskRunner;
import deviceet.sample.equipment.domain.EquipmentRepository;
import deviceet.sample.equipment.domain.event.EquipmentDeletedEvent;
import deviceet.sample.maintenance.domain.task.DeleteAllMaintenanceRecordsUnderEquipmentTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EquipmentDeletedEventEventHandler extends AbstractEventHandler<EquipmentDeletedEvent> {
    private final DeleteAllMaintenanceRecordsUnderEquipmentTask deleteAllMaintenanceRecordsUnderEquipmentTask;
    private final EquipmentRepository equipmentRepository;

    @Override
    public void handle(EquipmentDeletedEvent event) {
        TaskRunner.run(() -> {
            equipmentRepository.evictCachedEquipmentSummaries(event.getArOrgId());
            log.debug("Evicted equipment summaries cache for org[{}].", event.getArOrgId());
        });

        TaskRunner.run(() -> deleteAllMaintenanceRecordsUnderEquipmentTask.run(event.getEquipmentId()));
    }

    @Override
    public boolean isIdempotent() {
        return true;// This handler can run multiple times safely
    }

    @Override
    public boolean isTransactional() {
        return false; // Better not be transactional as it deletes multiple records which can exceed Mongo transaction restrictions
    }
}
