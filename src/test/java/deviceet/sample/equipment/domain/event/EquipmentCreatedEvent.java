package deviceet.sample.equipment.domain.event;

import deviceet.common.event.DomainEvent;
import deviceet.sample.equipment.domain.Equipment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

import static deviceet.common.event.DomainEventType.EQUIPMENT_CREATED_EVENT;
import static lombok.AccessLevel.PRIVATE;

@Getter
@TypeAlias("EQUIPMENT_CREATED_EVENT")
@NoArgsConstructor(access = PRIVATE)
public class EquipmentCreatedEvent extends DomainEvent {
    private String equipmentId;

    public EquipmentCreatedEvent(Equipment equipment) {
        super(EQUIPMENT_CREATED_EVENT, equipment);
        this.equipmentId = equipment.getId();
    }
}
