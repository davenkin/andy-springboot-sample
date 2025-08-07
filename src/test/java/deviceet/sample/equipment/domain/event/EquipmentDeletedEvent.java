package deviceet.sample.equipment.domain.event;

import deviceet.common.event.DomainEvent;
import deviceet.sample.equipment.domain.Equipment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

import static deviceet.common.event.DomainEventType.EQUIPMENT_DELETED_EVENT;
import static lombok.AccessLevel.PRIVATE;

@Getter
@TypeAlias("EQUIPMENT_DELETED_EVENT")
@NoArgsConstructor(access = PRIVATE)
public class EquipmentDeletedEvent extends DomainEvent {
    private String equipmentId;

    public EquipmentDeletedEvent(Equipment equipment) {
        super(EQUIPMENT_DELETED_EVENT, equipment);
        this.equipmentId = equipment.getId();
    }
}
