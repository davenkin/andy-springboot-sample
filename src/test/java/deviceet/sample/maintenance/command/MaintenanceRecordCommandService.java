package deviceet.sample.maintenance.command;


import deviceet.common.model.Principal;
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

    @Transactional
    public String createMaintenanceRecord(CreateMaintenanceRecordCommand command, Principal principal) {
        MaintenanceRecord record = maintenanceRecordFactory.create(command.equipmentId(),
                command.status(),
                command.description(),
                principal);
        maintenanceRecordRepository.save(record);
        log.info("Created MaintenanceRecord[{}].", record.getId());
        return record.getId();
    }
}
