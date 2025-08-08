package deviceet.sample.maintenance.domain;

import deviceet.common.model.AggregateRoot;
import deviceet.common.model.Principal;
import deviceet.sample.equipment.domain.EquipmentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import static deviceet.common.utils.SnowflakeIdGenerator.newSnowflakeId;
import static deviceet.sample.maintenance.domain.MaintenanceRecord.MAINTENANCE_RECORD_COLLECTION;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Getter
@FieldNameConstants
@TypeAlias(MAINTENANCE_RECORD_COLLECTION)
@Document(MAINTENANCE_RECORD_COLLECTION)
@NoArgsConstructor(access = PRIVATE)
public class MaintenanceRecord extends AggregateRoot {
    public final static String MAINTENANCE_RECORD_COLLECTION = "maintenance-record";

    private String equipmentId;
    private String equipmentName;
    private EquipmentStatus status;
    private String description;

    public MaintenanceRecord(String equipmentId,
                             String equipmentName,
                             EquipmentStatus status,
                             String description,
                             Principal principal) {
        super(newMaintenanceRecordId(), principal);
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.status = status;
        this.description = description;
    }

    public static String newMaintenanceRecordId() {
        return "MTR" + newSnowflakeId();
    }
}
