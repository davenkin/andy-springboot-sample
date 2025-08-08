package deviceet.sample.equipment.domain.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SyncEquipmentNameToMaintenanceRecordsTask {
    public long run(String equipmentId) {
        return 0;//todo:impl
    }
}
