package deviceet.sample.equipment.domain.event;

import deviceet.common.event.DomainEvent;
import deviceet.common.event.DomainEventType;
import deviceet.sample.equipment.domain.TestAr;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public abstract class TestArUpdatedEvent extends DomainEvent {
    private String testArId;

    public TestArUpdatedEvent(DomainEventType type, TestAr ar) {
        super(type, ar);
        this.testArId = ar.getId();
    }
}
