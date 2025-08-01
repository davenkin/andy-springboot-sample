package deviceet;

import deviceet.common.event.DomainEvent;
import deviceet.common.event.DomainEventType;
import deviceet.common.event.publish.PublishingDomainEvent;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

import static deviceet.common.utils.CommonUtils.mongoConcatFields;
import static deviceet.common.utils.CommonUtils.requireNonBlank;
import static deviceet.common.utils.Constants.CI_PROFILE;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@SuppressWarnings("unchecked")
@Execution(CONCURRENT)
@ActiveProfiles(CI_PROFILE)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class BaseTest {

    @Autowired
    protected MongoTemplate mongoTemplate;

    protected <T extends DomainEvent> T latestEventFor(String arId, DomainEventType type, Class<T> eventClass) {
        requireNonBlank(arId, "arId must not be blank.");
        requireNonNull(type, "type must not be null.");
        requireNonNull(eventClass, "eventClass must not be null.");

        Query query = query(where(mongoConcatFields(PublishingDomainEvent.Fields.event, DomainEvent.Fields.arId)).is(arId)
                .and(mongoConcatFields(PublishingDomainEvent.Fields.event, DomainEvent.Fields.type)).is(type))
                .with(by(DESC, PublishingDomainEvent.Fields.raisedAt));
        PublishingDomainEvent domainEvent = mongoTemplate.findOne(query, PublishingDomainEvent.class);
        return domainEvent == null ? null : (T) domainEvent.getEvent();
    }
}
