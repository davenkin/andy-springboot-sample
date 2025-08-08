package deviceet.sample.maintenance;

import deviceet.common.model.AggregateRoot;
import deviceet.sample.equipment.domain.EquipmentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import static deviceet.sample.maintenance.MaintenanceRecord.MAINTENANCE_RECORD_COLLECTION;
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
}
