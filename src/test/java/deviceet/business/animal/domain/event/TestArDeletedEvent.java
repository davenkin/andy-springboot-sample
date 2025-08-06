package deviceet.business.animal.domain.event;

import deviceet.business.animal.domain.TestAr;
import deviceet.common.event.DomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

import static deviceet.common.event.DomainEventType.TEST_AR_DELETED_EVENT;
import static lombok.AccessLevel.PRIVATE;

@Getter
@TypeAlias("TEST_AR_DELETED_EVENT")
@NoArgsConstructor(access = PRIVATE)
public class TestArDeletedEvent extends DomainEvent {
    private String testArId;

    public TestArDeletedEvent(TestAr ar) {
        super(TEST_AR_DELETED_EVENT, ar);
        this.testArId = ar.getId();
    }
}
