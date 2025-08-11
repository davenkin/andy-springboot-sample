package deviceet.sample.maintenance.command;


import deviceet.common.model.principal.Principal;
import deviceet.sample.equipment.domain.Equipment;
import deviceet.sample.equipment.domain.EquipmentRepository;
import deviceet.sample.maintenance.domain.MaintenanceRecord;
import deviceet.sample.maintenance.domain.MaintenanceRecordFactory;
import deviceet.sample.maintenance.domain.MaintenanceRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MaintenanceRecordCommandService {
    private final MaintenanceRecordFactory maintenanceRecordFactory;
    private final MaintenanceRecordRepository maintenanceRecordRepository;
    private final EquipmentRepository equipmentRepository;

    @Transactional
    public String createMaintenanceRecord(CreateMaintenanceRecordCommand command, Principal principal) {
        Equipment equipment = equipmentRepository.byId(command.equipmentId(), principal.getOrgId());
        MaintenanceRecord record = maintenanceRecordFactory.create(equipment,
                command.status(),
                command.description(),
                principal);
        maintenanceRecordRepository.save(record);
        log.info("Created MaintenanceRecord[{}].", record.getId());
        return record.getId();
    }

    @Transactional
    public void deleteMaintenanceRecord(String maintenanceRecordId, Principal principal) {
        MaintenanceRecord maintenanceRecord = maintenanceRecordRepository.byId(maintenanceRecordId, principal.getOrgId());
        maintenanceRecordRepository.delete(maintenanceRecord);
        log.info("Deleted MaintenanceRecord[{}].", maintenanceRecordId);
    }
}
