package deviceet.sample.maintenance;

import deviceet.sample.equipment.domain.EquipmentStatus;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public class MaintenanceRecord {
    private String equipmentId;
    private String equipmentName;
    private EquipmentStatus equipmentStatus;
    private String description;
}
