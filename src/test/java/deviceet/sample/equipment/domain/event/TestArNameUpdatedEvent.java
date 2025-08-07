package deviceet.sample.equipment.domain.event;

import deviceet.sample.equipment.domain.TestAr;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

import static deviceet.common.event.DomainEventType.TEST_AR_NAME_UPDATED_EVENT;
import static lombok.AccessLevel.PRIVATE;

@Getter
@TypeAlias("TEST_AR_NAME_UPDATED_EVENT")
@NoArgsConstructor(access = PRIVATE)
public class TestArNameUpdatedEvent extends TestArUpdatedEvent {
    private String updatedName;

    public TestArNameUpdatedEvent(String updatedName, TestAr ar) {
        super(TEST_AR_NAME_UPDATED_EVENT, ar);
        this.updatedName = updatedName;
    }
}
