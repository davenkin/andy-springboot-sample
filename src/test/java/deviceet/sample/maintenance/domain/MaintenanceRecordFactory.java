package deviceet.sample.maintenance.domain;

import deviceet.common.model.operator.Operator;
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
                                    Operator operator) {
        return new MaintenanceRecord(equipment.getId(), equipment.getName(), status, description, operator);
    }
}
