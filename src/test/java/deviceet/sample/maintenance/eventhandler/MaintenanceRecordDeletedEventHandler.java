package deviceet.sample.maintenance.eventhandler;

import deviceet.common.event.consume.AbstractEventHandler;
import deviceet.common.util.TaskRunner;
import deviceet.sample.equipment.domain.task.CountMaintenanceRecordsForEquipmentTask;
import deviceet.sample.maintenance.domain.event.MaintenanceRecordDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MaintenanceRecordDeletedEventHandler extends AbstractEventHandler<MaintenanceRecordDeletedEvent> {
    private final CountMaintenanceRecordsForEquipmentTask countMaintenanceRecordsForEquipmentTask;

    @Override
    public void handle(MaintenanceRecordDeletedEvent event) {
        TaskRunner.run(() -> countMaintenanceRecordsForEquipmentTask.run(event.getEquipmentId()));
    }
}
