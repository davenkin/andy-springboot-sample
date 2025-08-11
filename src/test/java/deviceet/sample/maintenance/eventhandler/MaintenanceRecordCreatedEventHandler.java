package deviceet.sample.maintenance.eventhandler;

import deviceet.common.event.consume.AbstractEventHandler;
import deviceet.common.util.ExceptionSwallowRunner;
import deviceet.sample.equipment.domain.EquipmentRepository;
import deviceet.sample.equipment.domain.task.CountMaintenanceRecordsForEquipmentTask;
import deviceet.sample.maintenance.domain.MaintenanceRecordRepository;
import deviceet.sample.maintenance.domain.event.MaintenanceRecordCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MaintenanceRecordCreatedEventHandler extends AbstractEventHandler<MaintenanceRecordCreatedEvent> {
    private final CountMaintenanceRecordsForEquipmentTask countMaintenanceRecordsForEquipmentTask;
    private final EquipmentRepository equipmentRepository;
    private final MaintenanceRecordRepository maintenanceRecordRepository;

    @Override
    public void handle(MaintenanceRecordCreatedEvent event) {
        ExceptionSwallowRunner.run(() -> countMaintenanceRecordsForEquipmentTask.run(event.getEquipmentId()));
        ExceptionSwallowRunner.run(() -> updateEquipmentStatus(event.getEquipmentId()));
    }

    private void updateEquipmentStatus(String equipmentId) {
        equipmentRepository.byIdOptional(equipmentId).ifPresent(equipment -> {
            maintenanceRecordRepository.latestFor(equipmentId).ifPresent(record -> {
                equipment.updateStatus(record.getStatus());
                equipmentRepository.save(equipment);
                log.info("Updated equipment[{}] status from its lasted maintenance record[{}].",
                        equipment.getId(), record.getId());
            });
        });
    }
}
