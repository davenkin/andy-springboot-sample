package deviceet.common.event.publish;

import deviceet.IntegrationTest;
import deviceet.business.testar.command.CreateTestArCommand;
import deviceet.business.testar.command.TestArCommandService;
import deviceet.business.testar.domain.event.TestArCreatedEvent;
import deviceet.common.event.publish.infrastructure.FakeDomainEventSender;
import deviceet.common.model.Principal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.concurrent.CompletableFuture;

import static deviceet.TestRandomUtils.randomPrincipal;
import static deviceet.TestRandomUtils.randomTestArName;
import static deviceet.common.event.DomainEventType.TEST_AR_CREATED_EVENT;
import static deviceet.common.event.publish.DomainEventPublishStatus.*;
import static java.util.concurrent.CompletableFuture.failedFuture;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DomainEventPublishJobIntegrationTest extends IntegrationTest {

    @Autowired
    private DomainEventPublishJob domainEventPublishJob;

    @Autowired
    private TestArCommandService testArCommandService;

    @Autowired
    private PublishingDomainEventDao publishingDomainEventDao;

    @MockitoSpyBean
    private FakeDomainEventSender domainEventSender;

    @Test
    public void should_publish_domain_events() {
        Principal principal = randomPrincipal();
        String arId1 = testArCommandService.createTestAr(CreateTestArCommand.builder().name(randomTestArName()).build(), principal);
        String arId2 = testArCommandService.createTestAr(CreateTestArCommand.builder().name(randomTestArName()).build(), principal);
        String arId3 = testArCommandService.createTestAr(CreateTestArCommand.builder().name(randomTestArName()).build(), principal);
        String arId4 = testArCommandService.createTestAr(CreateTestArCommand.builder().name(randomTestArName()).build(), principal);

        TestArCreatedEvent event1 = latestEventFor(arId1, TEST_AR_CREATED_EVENT, TestArCreatedEvent.class);
        TestArCreatedEvent event2 = latestEventFor(arId2, TEST_AR_CREATED_EVENT, TestArCreatedEvent.class);
        TestArCreatedEvent event3 = latestEventFor(arId3, TEST_AR_CREATED_EVENT, TestArCreatedEvent.class);
        TestArCreatedEvent event4 = latestEventFor(arId4, TEST_AR_CREATED_EVENT, TestArCreatedEvent.class);
        assertEquals(CREATED, publishingDomainEventDao.byId(event1.getId()).getStatus());
        assertEquals(CREATED, publishingDomainEventDao.byId(event2.getId()).getStatus());
        assertEquals(CREATED, publishingDomainEventDao.byId(event3.getId()).getStatus());
        assertEquals(CREATED, publishingDomainEventDao.byId(event4.getId()).getStatus());

        domainEventPublishJob.publishStagedDomainEvents(2);

        assertEquals(PUBLISH_SUCCEED, publishingDomainEventDao.byId(event1.getId()).getStatus());
        assertEquals(PUBLISH_SUCCEED, publishingDomainEventDao.byId(event2.getId()).getStatus());
        assertEquals(PUBLISH_SUCCEED, publishingDomainEventDao.byId(event3.getId()).getStatus());
        assertEquals(PUBLISH_SUCCEED, publishingDomainEventDao.byId(event4.getId()).getStatus());

        assertTrue(domainEventSender.getEvents().containsKey(event1.getId()));
        assertTrue(domainEventSender.getEvents().containsKey(event2.getId()));
        assertTrue(domainEventSender.getEvents().containsKey(event3.getId()));
        assertTrue(domainEventSender.getEvents().containsKey(event4.getId()));
        verify(domainEventSender, times(4)).send(any());
    }

    @Test
    public void should_fail_publish_domain_events_with_max_of_3_attempts() {
        Principal principal = randomPrincipal();
        String arId = testArCommandService.createTestAr(CreateTestArCommand.builder().name(randomTestArName()).build(), principal);
        TestArCreatedEvent event = latestEventFor(arId, TEST_AR_CREATED_EVENT, TestArCreatedEvent.class);
        doReturn(failedFuture(new RuntimeException("stub exception"))).when(domainEventSender).send(any());
        domainEventPublishJob.publishStagedDomainEvents(2);
        PublishingDomainEvent publishingDomainEvent1 = publishingDomainEventDao.byId(event.getId());
        assertEquals(PUBLISH_FAILED, publishingDomainEvent1.getStatus());
        assertEquals(1, publishingDomainEvent1.getPublishedCount());

        domainEventPublishJob.publishStagedDomainEvents(2);
        PublishingDomainEvent publishingDomainEvent2 = publishingDomainEventDao.byId(event.getId());
        assertEquals(PUBLISH_FAILED, publishingDomainEvent2.getStatus());
        assertEquals(2, publishingDomainEvent2.getPublishedCount());

        domainEventPublishJob.publishStagedDomainEvents(2);
        PublishingDomainEvent publishingDomainEvent3 = publishingDomainEventDao.byId(event.getId());
        assertEquals(PUBLISH_FAILED, publishingDomainEvent3.getStatus());
        assertEquals(3, publishingDomainEvent3.getPublishedCount());

        domainEventPublishJob.publishStagedDomainEvents(2);
        PublishingDomainEvent publishingDomainEvent4 = publishingDomainEventDao.byId(event.getId());
        assertEquals(PUBLISH_FAILED, publishingDomainEvent4.getStatus());
        assertEquals(3, publishingDomainEvent4.getPublishedCount());

        verify(domainEventSender, times(3)).send(any());
    }

    @Test
    public void should_publish_successfully_if_sender_recovered() {
        Principal principal = randomPrincipal();
        String arId = testArCommandService.createTestAr(CreateTestArCommand.builder().name(randomTestArName()).build(), principal);
        TestArCreatedEvent event = latestEventFor(arId, TEST_AR_CREATED_EVENT, TestArCreatedEvent.class);
        doReturn(failedFuture(new RuntimeException("stub exception")))
                .doReturn(CompletableFuture.completedFuture(event.getId()))
                .when(domainEventSender).send(any());

        domainEventPublishJob.publishStagedDomainEvents(2);
        PublishingDomainEvent publishingDomainEvent1 = publishingDomainEventDao.byId(event.getId());
        assertEquals(PUBLISH_FAILED, publishingDomainEvent1.getStatus());
        assertEquals(1, publishingDomainEvent1.getPublishedCount());

        domainEventPublishJob.publishStagedDomainEvents(2);
        PublishingDomainEvent publishingDomainEvent2 = publishingDomainEventDao.byId(event.getId());
        assertEquals(PUBLISH_SUCCEED, publishingDomainEvent2.getStatus());
        assertEquals(2, publishingDomainEvent2.getPublishedCount());
        verify(domainEventSender, times(2)).send(any());
    }

}