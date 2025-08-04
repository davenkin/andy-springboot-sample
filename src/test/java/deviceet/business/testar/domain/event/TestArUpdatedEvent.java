package deviceet.business.testar.domain.event;

import deviceet.business.testar.domain.TestAr;
import deviceet.common.event.DomainEvent;
import deviceet.common.event.DomainEventType;
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
