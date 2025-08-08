package deviceet.sample.maintenance.domain.event;

import deviceet.common.event.DomainEvent;
import deviceet.sample.maintenance.domain.MaintenanceRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

import static deviceet.common.event.DomainEventType.MAINTENANCE_RECORD_DELETED_EVENT;
import static lombok.AccessLevel.PRIVATE;

@Getter
@TypeAlias("MAINTENANCE_RECORD_DELETED_EVENT")
@NoArgsConstructor(access = PRIVATE)
public class MaintenanceRecordDeletedEvent extends DomainEvent {
    private String maintenanceRecordId;
    private String equipmentId;

    public MaintenanceRecordDeletedEvent(MaintenanceRecord record) {
        super(MAINTENANCE_RECORD_DELETED_EVENT, record);
        this.maintenanceRecordId = record.getId();
        this.equipmentId = record.getEquipmentId();
    }
}
