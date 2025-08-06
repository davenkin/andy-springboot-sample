package deviceet.common.event.consume;

import deviceet.IntegrationTest;
import deviceet.business.testar.command.CreateTestArCommand;
import deviceet.business.testar.command.TestArCommandService;
import deviceet.business.testar.command.UpdateTestArNameCommand;
import deviceet.business.testar.domain.event.TestArCreatedEvent;
import deviceet.business.testar.domain.event.TestArNameUpdatedEvent;
import deviceet.business.testar.eventhandler.TestArCreatedEventHandler;
import deviceet.business.testar.eventhandler.TestArCreatedEventHandler2;
import deviceet.business.testar.eventhandler.TestArNameUpdatedEventHandler;
import deviceet.business.testar.eventhandler.TestArUpdatedEventHandler;
import deviceet.common.model.Principal;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static deviceet.TestRandomUtils.randomPrincipal;
import static deviceet.TestRandomUtils.randomTestArName;
import static deviceet.common.event.DomainEventType.TEST_AR_CREATED_EVENT;
import static deviceet.common.event.DomainEventType.TEST_AR_NAME_UPDATED_EVENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventConsumerIntegrationTest extends IntegrationTest {

    @Autowired
    private TestArCommandService testArCommandService;

    @MockitoSpyBean
    private TestArCreatedEventHandler createdEventHandler;

    @MockitoSpyBean
    private TestArCreatedEventHandler2 createdEventHandler2;

    @MockitoSpyBean
    private TestArUpdatedEventHandler updatedEventHandler;

    @MockitoSpyBean
    private TestArNameUpdatedEventHandler nameUpdatedEventHandler;

    @MockitoSpyBean
    private ConsumingEventDao consumingEventDao;

    @Test
    public void should_only_handle_events_that_can_be_handled() {
        Principal principal = randomPrincipal();
        String arId = testArCommandService.createTestAr(randomCreateTestArCommand(), principal);
        TestArCreatedEvent createdEvent = latestEventFor(arId, TEST_AR_CREATED_EVENT, TestArCreatedEvent.class);

        eventConsumer.consumeDomainEvent(createdEvent);

        verify(createdEventHandler, times(1)).handle(any(TestArCreatedEvent.class));
        verify(updatedEventHandler, times(0)).handle(any());
        verify(consumingEventDao).markEventAsConsumedByHandler(any(ConsumingEvent.class), any(TestArCreatedEventHandler.class));
        assertTrue(consumingEventDao.exists(createdEvent.getId()));
    }

    @Test
    public void should_call_handler_for_event_hierarchy() {
        Principal principal = randomPrincipal();
        String arId = testArCommandService.createTestAr(randomCreateTestArCommand(), principal);
        testArCommandService.updateTestArName(arId, randomUpdateTestArNameCommand(), principal);
        TestArNameUpdatedEvent updatedEvent = latestEventFor(arId, TEST_AR_NAME_UPDATED_EVENT, TestArNameUpdatedEvent.class);

        eventConsumer.consumeDomainEvent(updatedEvent);

        verify(nameUpdatedEventHandler).handle(any(TestArNameUpdatedEvent.class));
        verify(updatedEventHandler).handle(any(TestArNameUpdatedEvent.class));
        assertTrue(consumingEventDao.exists(updatedEvent.getId(), nameUpdatedEventHandler));
        assertTrue(consumingEventDao.exists(updatedEvent.getId(), updatedEventHandler));
    }

    @Test
    public void multiple_handlers_should_run_in_order_of_priority() {
        Principal principal = randomPrincipal();
        String arId = testArCommandService.createTestAr(randomCreateTestArCommand(), principal);
        TestArCreatedEvent createdEvent = latestEventFor(arId, TEST_AR_CREATED_EVENT, TestArCreatedEvent.class);
        when(createdEventHandler.priority()).thenReturn(0);
        when(createdEventHandler2.priority()).thenReturn(1);

        eventConsumer.consumeDomainEvent(createdEvent);

        InOrder inOrder = Mockito.inOrder(createdEventHandler, createdEventHandler2);
        inOrder.verify(createdEventHandler).handle(any(TestArCreatedEvent.class));
        inOrder.verify(createdEventHandler2).handle(any(TestArCreatedEvent.class));
        assertTrue(consumingEventDao.exists(createdEvent.getId(), createdEventHandler));
        assertTrue(consumingEventDao.exists(createdEvent.getId(), createdEventHandler2));
    }

    @Test
    public void multiple_handlers_should_run_in_order_of_priority_reversely() {
        Principal principal = randomPrincipal();
        String arId = testArCommandService.createTestAr(randomCreateTestArCommand(), principal);
        TestArCreatedEvent createdEvent = latestEventFor(arId, TEST_AR_CREATED_EVENT, TestArCreatedEvent.class);
        when(createdEventHandler.priority()).thenReturn(1);
        when(createdEventHandler2.priority()).thenReturn(0);

        eventConsumer.consumeDomainEvent(createdEvent);

        InOrder inOrder = Mockito.inOrder(createdEventHandler, createdEventHandler2);
        inOrder.verify(createdEventHandler2).handle(any(TestArCreatedEvent.class));
        inOrder.verify(createdEventHandler).handle(any(TestArCreatedEvent.class));
        assertTrue(consumingEventDao.exists(createdEvent.getId(), createdEventHandler));
        assertTrue(consumingEventDao.exists(createdEvent.getId(), createdEventHandler2));
    }

    @Test
    public void should_mark_as_consumed_if_non_transactional_handler_throws_exception() {
        Principal principal = randomPrincipal();
        String arId = testArCommandService.createTestAr(randomCreateTestArCommand(), principal);
        TestArCreatedEvent createdEvent = latestEventFor(arId, TEST_AR_CREATED_EVENT, TestArCreatedEvent.class);
        doThrow(new RuntimeException("stub exception")).when(createdEventHandler).handle(any(TestArCreatedEvent.class));
        when(createdEventHandler.isTransactional()).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> eventConsumer.consumeDomainEvent(createdEvent));

        assertTrue(exception.getMessage().startsWith("Error while consuming event"));
        verify(consumingEventDao).markEventAsConsumedByHandler(any(ConsumingEvent.class), any(TestArCreatedEventHandler.class));
        assertTrue(consumingEventDao.exists(createdEvent.getId()));
    }

    @Test
    public void should_not_mark_as_consumed_if_transactional_handler_throws_exception() {
        Principal principal = randomPrincipal();
        String arId = testArCommandService.createTestAr(randomCreateTestArCommand(), principal);
        TestArCreatedEvent createdEvent = latestEventFor(arId, TEST_AR_CREATED_EVENT, TestArCreatedEvent.class);
        doThrow(new RuntimeException("stub exception")).when(createdEventHandler).handle(any(TestArCreatedEvent.class));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> eventConsumer.consumeDomainEvent(createdEvent));

        assertTrue(exception.getMessage().startsWith("Error while consuming event"));
        verify(consumingEventDao).markEventAsConsumedByHandler(any(ConsumingEvent.class), any(TestArCreatedEventHandler.class));
        assertFalse(consumingEventDao.exists(createdEvent.getId(), createdEventHandler));
    }

    @Test
    public void should_not_mark_as_consumed_for_idempotent_handler() {
        Principal principal = randomPrincipal();
        String arId = testArCommandService.createTestAr(randomCreateTestArCommand(), principal);
        TestArCreatedEvent createdEvent = latestEventFor(arId, TEST_AR_CREATED_EVENT, TestArCreatedEvent.class);
        when(createdEventHandler.isIdempotent()).thenReturn(true);

        eventConsumer.consumeDomainEvent(createdEvent);

        verify(consumingEventDao, times(0)).markEventAsConsumedByHandler(any(), any(TestArCreatedEventHandler.class));
        verify(consumingEventDao, times(1)).markEventAsConsumedByHandler(any(), any(TestArCreatedEventHandler2.class));
        assertFalse(consumingEventDao.exists(createdEvent.getId(), createdEventHandler));
        verify(createdEventHandler, times(1)).handle(any(TestArCreatedEvent.class));
    }

    @Test
    public void multiple_handlers_should_run_independently() {
        Principal principal = randomPrincipal();
        String arId = testArCommandService.createTestAr(randomCreateTestArCommand(), principal);
        testArCommandService.updateTestArName(arId, randomUpdateTestArNameCommand(), principal);
        TestArNameUpdatedEvent updatedEvent = latestEventFor(arId, TEST_AR_NAME_UPDATED_EVENT, TestArNameUpdatedEvent.class);
        when(nameUpdatedEventHandler.priority()).thenReturn(0);
        when(updatedEventHandler.priority()).thenReturn(1);
        doThrow(new RuntimeException("stub exception")).when(nameUpdatedEventHandler).handle(any(TestArNameUpdatedEvent.class));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> eventConsumer.consumeDomainEvent(updatedEvent));

        assertTrue(exception.getMessage().startsWith("Error while consuming event"));
        verify(nameUpdatedEventHandler).handle(any(TestArNameUpdatedEvent.class));
        verify(updatedEventHandler).handle(any(TestArNameUpdatedEvent.class));
    }

    @Test
    public void should_run_again_for_idempotent_handler() {
        Principal principal = randomPrincipal();
        String arId = testArCommandService.createTestAr(randomCreateTestArCommand(), principal);
        TestArCreatedEvent createdEvent = latestEventFor(arId, TEST_AR_CREATED_EVENT, TestArCreatedEvent.class);
        when(createdEventHandler.isIdempotent()).thenReturn(true);

        eventConsumer.consumeDomainEvent(createdEvent);
        eventConsumer.consumeDomainEvent(createdEvent);

        verify(createdEventHandler, times(2)).handle(any(TestArCreatedEvent.class));
    }

    @Test
    public void should_not_handle_again_for_non_idempotent_handler() {
        Principal principal = randomPrincipal();
        String arId = testArCommandService.createTestAr(randomCreateTestArCommand(), principal);
        TestArCreatedEvent createdEvent = latestEventFor(arId, TEST_AR_CREATED_EVENT, TestArCreatedEvent.class);
        when(createdEventHandler.isIdempotent()).thenReturn(false);

        eventConsumer.consumeDomainEvent(createdEvent);
        eventConsumer.consumeDomainEvent(createdEvent);

        verify(consumingEventDao, times(2)).markEventAsConsumedByHandler(any(), any(TestArCreatedEventHandler.class));
        verify(createdEventHandler, times(1)).handle(any(TestArCreatedEvent.class));
    }


    private static UpdateTestArNameCommand randomUpdateTestArNameCommand() {
        return new UpdateTestArNameCommand(randomTestArName());
    }

    private static CreateTestArCommand randomCreateTestArCommand() {
        return new CreateTestArCommand(randomTestArName());
    }

}