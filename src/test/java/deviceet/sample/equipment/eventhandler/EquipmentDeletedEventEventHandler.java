package deviceet.sample.equipment.eventhandler;

import deviceet.common.event.consume.AbstractEventHandler;
import deviceet.common.utils.TaskRunner;
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
            long deletedCount = deleteAllMaintenanceRecordsUnderEquipmentTask.run(event.getEquipmentId());
            log.info("Delete all {} maintenance records under equipment [{}].", deletedCount, event.getEquipmentId());
        });

        TaskRunner.run(() -> {
            equipmentRepository.evictCachedEquipmentSummaries(event.getArOrgId());
            log.debug("Evicted equipment summaries cache for org[{}].", event.getArOrgId());
        });
    }
}
