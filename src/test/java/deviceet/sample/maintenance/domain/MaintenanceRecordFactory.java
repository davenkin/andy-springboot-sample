package deviceet.sample.maintenance.domain;

import deviceet.common.model.principal.Principal;
import deviceet.sample.equipment.domain.Equipment;
import deviceet.sample.equipment.domain.EquipmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MaintenanceRecordFactory {

    public MaintenanceRecord create(Equipment equipment,
                                    EquipmentStatus status,
                                    String description,
                                    Principal principal) {
        return new MaintenanceRecord(equipment.getId(), equipment.getName(), status, description, principal);
    }
}
