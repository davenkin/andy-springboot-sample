package com.company.andy.sample.maintenance.eventhandler;

import com.company.andy.common.event.consume.AbstractEventHandler;
import com.company.andy.common.util.ExceptionSwallowRunner;
import com.company.andy.sample.equipment.domain.EquipmentRepository;
import com.company.andy.sample.equipment.domain.task.CountMaintenanceRecordsForEquipmentTask;
import com.company.andy.sample.maintenance.domain.MaintenanceRecordRepository;
import com.company.andy.sample.maintenance.domain.event.MaintenanceRecordCreatedEvent;
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
