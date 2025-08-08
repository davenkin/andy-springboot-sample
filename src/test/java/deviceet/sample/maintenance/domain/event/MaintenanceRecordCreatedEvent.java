package deviceet.sample.maintenance.domain.event;

import deviceet.common.event.DomainEvent;
import deviceet.sample.maintenance.domain.MaintenanceRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

import static deviceet.common.event.DomainEventType.MAINTENANCE_RECORD_CREATED_EVENT;
import static lombok.AccessLevel.PRIVATE;

@Getter
@TypeAlias("MAINTENANCE_RECORD_CREATED_EVENT")
@NoArgsConstructor(access = PRIVATE)
public class MaintenanceRecordCreatedEvent extends DomainEvent {
    private String maintenanceRecordId;
    private String equipmentId;
    private String equipmentName;

    public MaintenanceRecordCreatedEvent(MaintenanceRecord maintenanceRecord) {
        super(MAINTENANCE_RECORD_CREATED_EVENT, maintenanceRecord);
        this.maintenanceRecordId = maintenanceRecord.getId();
        this.equipmentId = maintenanceRecord.getEquipmentId();
        this.equipmentName = maintenanceRecord.getEquipmentName();
    }
}
