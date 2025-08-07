package deviceet.sample.equipment.eventhandler;

import deviceet.common.event.consume.AbstractEventHandler;
import deviceet.common.utils.TaskRunner;
import deviceet.sample.equipment.domain.event.EquipmentDeletedEvent;
import deviceet.sample.equipment.job.task.DeleteAllMaintenanceRecordsUnderEquipmentTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EquipmentDeletedEventEventHandler extends AbstractEventHandler<EquipmentDeletedEvent> {
    private final DeleteAllMaintenanceRecordsUnderEquipmentTask deleteAllMaintenanceRecordsUnderEquipmentTask;

    @Override
    public void handle(EquipmentDeletedEvent event) {
        TaskRunner.run(() -> deleteAllMaintenanceRecordsUnderEquipmentTask.run(event.getEquipmentId()));
        // todo: evict all equipment cache
    }
}
