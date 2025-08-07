package deviceet.sample.equipment.domain.event;

import deviceet.sample.equipment.domain.Equipment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

import static deviceet.common.event.DomainEventType.EQUIPMENT_NAME_UPDATED_EVENT;
import static lombok.AccessLevel.PRIVATE;

@Getter
@TypeAlias("EQUIPMENT_NAME_UPDATED_EVENT")
@NoArgsConstructor(access = PRIVATE)
public class EquipmentNameUpdatedEvent extends EquipmentUpdatedEvent {
    private String updatedName;

    public EquipmentNameUpdatedEvent(String updatedName, Equipment equipment) {
        super(EQUIPMENT_NAME_UPDATED_EVENT, equipment);
        this.updatedName = updatedName;
    }
}
