package deviceet.common.event;

import com.mongodb.client.result.DeleteResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import static deviceet.common.event.consume.ConsumingEvent.Fields.consumedAt;
import static deviceet.common.event.publish.PublishingDomainEvent.Fields.raisedAt;
import static deviceet.common.util.Constants.CONSUMING_EVENT_COLLECTION;
import static deviceet.common.util.Constants.PUBLISHING_EVENT_COLLECTION;
import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Slf4j
@Component
@RequiredArgsConstructor
public class DomainEventHouseKeepingJob {
    private final MongoTemplate mongoTemplate;

    @Retryable(backoff = @Backoff(delay = 1000, multiplier = 3))
    public void removeOldPublishingDomainEventsFromMongo(int days) {
        log.info("Start remove old publishing domain events from mongodb.");
        Query query = Query.query(where(raisedAt).lt(now().minus(days, DAYS)));
        DeleteResult result = mongoTemplate.remove(query, PUBLISHING_EVENT_COLLECTION);
        log.info("Removed {} old publishing domain events from mongodb which are more than {} days old.", result.getDeletedCount(), days);
    }

    @Retryable(backoff = @Backoff(delay = 1000, multiplier = 3))
    public void removeOldConsumingDomainEventsFromMongo(int days) {
        log.info("Start remove old consuming domain events from mongodb.");
        Query query = Query.query(where(consumedAt).lt(now().minus(days, DAYS)));
        DeleteResult result = mongoTemplate.remove(query, CONSUMING_EVENT_COLLECTION);
        log.info("Removed {} old consuming domain events from mongodb which are more than {} days old.", result.getDeletedCount(), days);
    }

}
