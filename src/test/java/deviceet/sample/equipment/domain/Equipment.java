package deviceet.sample.equipment.domain;

import deviceet.common.model.AggregateRoot;
import deviceet.common.model.Principal;
import deviceet.sample.equipment.domain.event.TestArCreatedEvent;
import deviceet.sample.equipment.domain.event.TestArDeletedEvent;
import deviceet.sample.equipment.domain.event.TestArNameUpdatedEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

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

    public Equipment(String name, Principal principal) {
        super(newTestArId(), principal);
        this.name = name;
        raiseEvent(new TestArCreatedEvent(this));
    }

    public static String newTestArId() {
        return "TESTAR" + newSnowflakeId();
    }

    public void updateName(String newName) {
        this.name = newName;
        raiseEvent(new TestArNameUpdatedEvent(name, this));
    }

    @Override
    public void onDelete() {
        raiseEvent(new TestArDeletedEvent(this));
    }
}
