package deviceet.common.event;

import deviceet.IntegrationTest;
import deviceet.business.sampledevice.domain.TestAr;
import deviceet.business.sampledevice.domain.event.TestArCreatedEvent;
import deviceet.business.sampledevice.eventhandler.TestArCreatedEventHandler;
import deviceet.common.event.consume.ConsumingEvent;
import deviceet.common.event.consume.ConsumingEventDao;
import deviceet.common.event.publish.PublishingDomainEventDao;
import deviceet.common.model.Principal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static deviceet.TestRandomUtils.randomPrincipal;
import static deviceet.TestRandomUtils.randomTestArName;
import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.*;

class DomainEventHouseKeepingJobIntegrationTest extends IntegrationTest {
    @Autowired
    private ConsumingEventDao consumingEventDao;

    @Autowired
    private PublishingDomainEventDao publishingDomainEventDao;

    @Autowired
    private DomainEventHouseKeepingJob domainEventHouseKeepingJob;

    @Autowired
    private TestArCreatedEventHandler testArCreatedEventHandler;

    @Test
    void should_remove_old_publishing_domain_events_from_mongo() {
        Principal principal = randomPrincipal();
        TestArCreatedEvent event1 = new TestArCreatedEvent(new TestAr(randomTestArName(), principal));
        TestArCreatedEvent event2 = new TestArCreatedEvent(new TestAr(randomTestArName(), principal));
        ReflectionTestUtils.setField(event1, DomainEvent.Fields.raisedAt, now().minus(110, DAYS));
        ReflectionTestUtils.setField(event2, DomainEvent.Fields.raisedAt, now().minus(90, DAYS));
        publishingDomainEventDao.stage(List.of(event1, event2));
        assertNotNull(publishingDomainEventDao.byId(event1.getId()));
        assertNotNull(publishingDomainEventDao.byId(event2.getId()));

        domainEventHouseKeepingJob.removeOldPublishingDomainEventsFromMongo(100);

        assertNull(publishingDomainEventDao.byId(event1.getId()));
        assertNotNull(publishingDomainEventDao.byId(event2.getId()));
    }

    @Test
    void should_remove_old_consuming_domain_events_from_mongo() {
        Principal principal = randomPrincipal();
        TestArCreatedEvent event1 = new TestArCreatedEvent(new TestAr(randomTestArName(), principal));
        TestArCreatedEvent event2 = new TestArCreatedEvent(new TestAr(randomTestArName(), principal));
        ConsumingEvent consumingEvent1 = new ConsumingEvent(event1.getId(), event1);
        ConsumingEvent consumingEvent2 = new ConsumingEvent(event2.getId(), event1);
        ReflectionTestUtils.setField(consumingEvent1, ConsumingEvent.Fields.consumedAt, now().minus(110, DAYS));
        ReflectionTestUtils.setField(consumingEvent2, ConsumingEvent.Fields.consumedAt, now().minus(90, DAYS));
        consumingEventDao.markEventAsConsumedByHandler(consumingEvent1, testArCreatedEventHandler);
        consumingEventDao.markEventAsConsumedByHandler(consumingEvent2, testArCreatedEventHandler);
        assertTrue(consumingEventDao.exists(event1.getId(), testArCreatedEventHandler));
        assertTrue(consumingEventDao.exists(event2.getId(), testArCreatedEventHandler));

        domainEventHouseKeepingJob.removeOldConsumingDomainEventsFromMongo(100);

        assertFalse(consumingEventDao.exists(event1.getId(), testArCreatedEventHandler));
        assertTrue(consumingEventDao.exists(event2.getId(), testArCreatedEventHandler));
    }

}