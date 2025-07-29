package deviceet.common.event.consume;

import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import static deviceet.common.event.consume.ConsumingEvent.Fields.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

// Upon consuming, record the event in DB to avoid duplicated event consuming
@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumingEventDao<T> {
    private final MongoTemplate mongoTemplate;

    // return true means this event has never been consumed before
    public boolean recordAsConsumed(ConsumingEvent<T> consumingEvent, AbstractEventHandler<T> handler) {
        Query query = query(where(eventId).is(consumingEvent.getEventId()).and(ConsumingEvent.Fields.handler).is(handler.getName()));

        Update update = new Update()
                .setOnInsert(type, consumingEvent.getType())
                .setOnInsert(consumedAt, consumingEvent.getConsumedAt());

        UpdateResult result = this.mongoTemplate.update(ConsumingEvent.class)
                .matching(query)
                .apply(update)
                .upsert();

        return result.getMatchedCount() == 0;
    }
}
