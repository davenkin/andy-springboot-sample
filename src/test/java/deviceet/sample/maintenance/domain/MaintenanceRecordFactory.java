package deviceet.sample.maintenance.domain;

import deviceet.common.model.principal.Principal;
import deviceet.sample.equipment.domain.Equipment;
import deviceet.sample.equipment.domain.EquipmentRepository;
import deviceet.sample.equipment.domain.EquipmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MaintenanceRecordFactory {
    private final EquipmentRepository equipmentRepository;

    public MaintenanceRecord create(String equipmentId,
                                    EquipmentStatus status,
                                    String description,
                                    Principal principal) {
        Equipment equipment = equipmentRepository.byId(equipmentId, principal.getOrgId());
        return new MaintenanceRecord(equipmentId, equipment.getName(), status, description, principal);
    }
}
