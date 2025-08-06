package deviceet.business.testar.domain;

import deviceet.business.testar.domain.event.TestArCreatedEvent;
import deviceet.business.testar.domain.event.TestArDeletedEvent;
import deviceet.business.testar.domain.event.TestArNameUpdatedEvent;
import deviceet.common.model.AggregateRoot;
import deviceet.common.security.Principal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import static deviceet.business.testar.domain.TestAr.TEST_AR_COLLECTION;
import static deviceet.common.utils.SnowflakeIdGenerator.newSnowflakeId;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Getter
@FieldNameConstants
@TypeAlias(TEST_AR_COLLECTION)
@Document(TEST_AR_COLLECTION)
@NoArgsConstructor(access = PRIVATE)
public class TestAr extends AggregateRoot {
    public final static String TEST_AR_COLLECTION = "TEST_AR_COLLECTION";
    private String name;

    public TestAr(String name, Principal principal) {
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
