package deviceet;

import deviceet.common.event.DomainEvent;
import deviceet.common.event.DomainEventType;
import deviceet.common.event.consume.EventConsumer;
import deviceet.common.event.publish.PublishingDomainEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import redis.embedded.RedisServer;

import java.io.IOException;

import static deviceet.common.utils.CommonUtils.mongoConcatFields;
import static deviceet.common.utils.CommonUtils.requireNonBlank;
import static deviceet.common.utils.Constants.IT_PROFILE;
import static java.util.Objects.requireNonNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Slf4j
@ActiveProfiles(IT_PROFILE)
@SpringBootTest(webEnvironment = RANDOM_PORT)
//@TestPropertySource(properties = "spring.autoconfigure.exclude=de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration")
public abstract class IntegrationTest {
    private static RedisServer redisServer;

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Autowired
    protected StringRedisTemplate stringRedisTemplate;

    @Autowired
    protected EventConsumer eventConsumer;

    @BeforeAll
    public static void beforeAll() {
        try {
            redisServer = new RedisServer(6124);
            redisServer.start();
        } catch (IOException e) {
            log.error("Failed to start RedisServer.", e);
        }
    }

    @AfterAll
    public static void afterAll() throws IOException {
        redisServer.stop();
    }

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
