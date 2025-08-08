package deviceet.sample.equipment.domain;

import deviceet.common.model.AggregateRoot;
import deviceet.common.model.Principal;
import deviceet.sample.equipment.domain.event.EquipmentCreatedEvent;
import deviceet.sample.equipment.domain.event.EquipmentDeletedEvent;
import deviceet.sample.equipment.domain.event.EquipmentNameUpdatedEvent;
import deviceet.sample.equipment.domain.event.EquipmentStatusUpdatedEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

import static deviceet.common.utils.SnowflakeIdGenerator.newSnowflakeId;
import static deviceet.sample.equipment.domain.Equipment.EQUIPMENT_COLLECTION;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Getter
@FieldNameConstants
@TypeAlias(EQUIPMENT_COLLECTION)
@Document(EQUIPMENT_COLLECTION)
@NoArgsConstructor(access = PRIVATE)
public class Equipment extends AggregateRoot {
    public final static String EQUIPMENT_COLLECTION = "equipment";
    private String name;
    private EquipmentStatus status;

    public Equipment(String name, Principal principal) {
        super(newEquipmentId(), principal);
        this.name = name;
        raiseEvent(new EquipmentCreatedEvent(this));
    }

    public static String newEquipmentId() {
        return "EQP" + newSnowflakeId();
    }

    public void updateName(String newName) {
        if (Objects.equals(newName, this.name)) {
            return;
        }
        this.name = newName;
        raiseEvent(new EquipmentNameUpdatedEvent(name, this));
    }

    public void updateStatus(EquipmentStatus status) {
        if (this.status == status) {
            return;
        }
        this.status = status;
        raiseEvent(new EquipmentStatusUpdatedEvent(this.status, this));

    }

    @Override
    public void onDelete() {
        raiseEvent(new EquipmentDeletedEvent(this));
    }
}
