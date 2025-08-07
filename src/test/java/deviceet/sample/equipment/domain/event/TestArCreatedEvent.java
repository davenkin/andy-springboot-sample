package deviceet.sample.equipment.domain.event;

import deviceet.common.event.DomainEvent;
import deviceet.sample.equipment.domain.TestAr;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

import static deviceet.common.event.DomainEventType.TEST_AR_CREATED_EVENT;
import static lombok.AccessLevel.PRIVATE;

@Getter
@TypeAlias("TEST_AR_CREATED_EVENT")
@NoArgsConstructor(access = PRIVATE)
public class TestArCreatedEvent extends DomainEvent {
    private String testArId;

    public TestArCreatedEvent(TestAr ar) {
        super(TEST_AR_CREATED_EVENT, ar);
        this.testArId = ar.getId();
    }
}
