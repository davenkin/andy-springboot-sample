package deviceet.sample.equipment.domain.event;

import deviceet.common.event.DomainEvent;
import deviceet.common.event.DomainEventType;
import deviceet.sample.equipment.domain.Equipment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public abstract class EquipmentUpdatedEvent extends DomainEvent {
    private String equipmentId;

    public EquipmentUpdatedEvent(DomainEventType type, Equipment equipment) {
        super(type, equipment);
        this.equipmentId = equipment.getId();
    }
}
