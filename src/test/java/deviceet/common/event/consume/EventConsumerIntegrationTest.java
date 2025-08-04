package deviceet.common.event.consume;

import deviceet.IntegrationTest;
import deviceet.common.security.Principal;
import deviceet.testar.command.CreateTestArCommand;
import deviceet.testar.command.TestArCommandService;
import deviceet.testar.command.UpdateTestArNameCommand;
import deviceet.testar.domain.event.TestArCreatedEvent;
import deviceet.testar.domain.event.TestArNameUpdatedEvent;
import deviceet.testar.eventhandler.TestArCreatedEventHandler;
import deviceet.testar.eventhandler.TestArNameUpdatedEventHandler;
import deviceet.testar.eventhandler.TestArUpdatedEventHandler;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static deviceet.TestRandomUtil.randomPrincipal;
import static deviceet.TestRandomUtil.randomTestArName;
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
    private TestArUpdatedEventHandler updatedEventHandler;

    @MockitoSpyBean
    private TestArNameUpdatedEventHandler nameUpdatedEventHandler;

    @MockitoSpyBean
    private ConsumingEventDao consumingEventDao;

    @Test
    public void should_only_handle_events_that_can_be_handled() {
        Principal principal = randomPrincipal();
        String testArId = testArCommandService.createTestAr(randomCreateTestArCommand(), principal);

        TestArCreatedEvent testArCreatedEvent = latestEventFor(testArId, TEST_AR_CREATED_EVENT, TestArCreatedEvent.class);
        eventConsumer.consumeDomainEvent(testArCreatedEvent);

        verify(createdEventHandler, times(1)).handle(any(TestArCreatedEvent.class));
        verify(updatedEventHandler, times(0)).handle(any());
        verify(consumingEventDao).markEventAsConsumedByHandler(any(ConsumingEvent.class), any(TestArCreatedEventHandler.class));
        assertTrue(consumingEventDao.exists(testArCreatedEvent.getId()));
    }

    @Test
    public void should_call_handler_based_on_priority() {
        Principal principal = randomPrincipal();
        String testArId = testArCommandService.createTestAr(randomCreateTestArCommand(), principal);
        testArCommandService.updateTestArName(testArId, randomUpdateTestArNameCommand(), principal);
        TestArNameUpdatedEvent testArNameUpdatedEvent = latestEventFor(testArId, TEST_AR_NAME_UPDATED_EVENT, TestArNameUpdatedEvent.class);
        when(nameUpdatedEventHandler.priority()).thenReturn(0);
        when(updatedEventHandler.priority()).thenReturn(1);

        eventConsumer.consumeDomainEvent(testArNameUpdatedEvent);

        InOrder inOrder = Mockito.inOrder(nameUpdatedEventHandler, updatedEventHandler);
        inOrder.verify(nameUpdatedEventHandler).handle(any(TestArNameUpdatedEvent.class));
        inOrder.verify(updatedEventHandler).handle(any(TestArNameUpdatedEvent.class));
        assertTrue(consumingEventDao.exists(testArNameUpdatedEvent.getId(), nameUpdatedEventHandler));
        assertTrue(consumingEventDao.exists(testArNameUpdatedEvent.getId(), updatedEventHandler));
    }

    @Test
    public void should_call_handler_based_on_priority_reversely() {
        Principal principal = randomPrincipal();
        String testArId = testArCommandService.createTestAr(randomCreateTestArCommand(), principal);
        testArCommandService.updateTestArName(testArId, randomUpdateTestArNameCommand(), principal);
        TestArNameUpdatedEvent testArNameUpdatedEvent = latestEventFor(testArId, TEST_AR_NAME_UPDATED_EVENT, TestArNameUpdatedEvent.class);
        when(nameUpdatedEventHandler.priority()).thenReturn(1);
        when(updatedEventHandler.priority()).thenReturn(0);

        eventConsumer.consumeDomainEvent(testArNameUpdatedEvent);

        InOrder inOrder = Mockito.inOrder(nameUpdatedEventHandler, updatedEventHandler);
        inOrder.verify(updatedEventHandler).handle(any(TestArNameUpdatedEvent.class));
        inOrder.verify(nameUpdatedEventHandler).handle(any(TestArNameUpdatedEvent.class));
        assertTrue(consumingEventDao.exists(testArNameUpdatedEvent.getId(), nameUpdatedEventHandler));
        assertTrue(consumingEventDao.exists(testArNameUpdatedEvent.getId(), updatedEventHandler));
    }

    @Test
    public void should_not_mark_as_consumed_if_transactional_handler_throws_exception() {
        Principal principal = randomPrincipal();
        String testArId = testArCommandService.createTestAr(randomCreateTestArCommand(), principal);
        TestArCreatedEvent testArCreatedEvent = latestEventFor(testArId, TEST_AR_CREATED_EVENT, TestArCreatedEvent.class);
        doThrow(new RuntimeException("stub exception")).when(createdEventHandler).handle(any(TestArCreatedEvent.class));
        when(createdEventHandler.isTransactional()).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> eventConsumer.consumeDomainEvent(testArCreatedEvent));

        assertTrue(exception.getMessage().startsWith("Error while consuming event"));
        verify(consumingEventDao).markEventAsConsumedByHandler(any(ConsumingEvent.class), any(TestArCreatedEventHandler.class));
        assertTrue(consumingEventDao.exists(testArCreatedEvent.getId()));
    }

    @Test
    public void should_mark_as_consumed_if_non_transactional_handler_throws_exception() {
        Principal principal = randomPrincipal();
        String testArId = testArCommandService.createTestAr(randomCreateTestArCommand(), principal);
        TestArCreatedEvent testArCreatedEvent = latestEventFor(testArId, TEST_AR_CREATED_EVENT, TestArCreatedEvent.class);
        doThrow(new RuntimeException("stub exception")).when(createdEventHandler).handle(any(TestArCreatedEvent.class));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> eventConsumer.consumeDomainEvent(testArCreatedEvent));

        assertTrue(exception.getMessage().startsWith("Error while consuming event"));
        verify(consumingEventDao).markEventAsConsumedByHandler(any(ConsumingEvent.class), any(TestArCreatedEventHandler.class));
        assertFalse(consumingEventDao.exists(testArCreatedEvent.getId()));
    }

    @Test
    public void should_not_mark_as_consumed_for_idempotent_handler() {
        Principal principal = randomPrincipal();
        String testArId = testArCommandService.createTestAr(randomCreateTestArCommand(), principal);
        TestArCreatedEvent testArCreatedEvent = latestEventFor(testArId, TEST_AR_CREATED_EVENT, TestArCreatedEvent.class);
        when(createdEventHandler.isIdempotent()).thenReturn(true);

        eventConsumer.consumeDomainEvent(testArCreatedEvent);

        verify(consumingEventDao, times(0)).markEventAsConsumedByHandler(any(), any());
        assertFalse(consumingEventDao.exists(testArCreatedEvent.getId()));
        verify(createdEventHandler, times(1)).handle(any(TestArCreatedEvent.class));
    }

    @Test
    public void multiple_handler_should_run_independently() {
        Principal principal = randomPrincipal();
        String testArId = testArCommandService.createTestAr(randomCreateTestArCommand(), principal);
        testArCommandService.updateTestArName(testArId, randomUpdateTestArNameCommand(), principal);
        TestArNameUpdatedEvent testArNameUpdatedEvent = latestEventFor(testArId, TEST_AR_NAME_UPDATED_EVENT, TestArNameUpdatedEvent.class);
        when(nameUpdatedEventHandler.priority()).thenReturn(0);
        when(updatedEventHandler.priority()).thenReturn(1);
        doThrow(new RuntimeException("stub exception")).when(nameUpdatedEventHandler).handle(any(TestArNameUpdatedEvent.class));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> eventConsumer.consumeDomainEvent(testArNameUpdatedEvent));

        assertTrue(exception.getMessage().startsWith("Error while consuming event"));
        verify(nameUpdatedEventHandler).handle(any(TestArNameUpdatedEvent.class));
        verify(updatedEventHandler).handle(any(TestArNameUpdatedEvent.class));
    }

    private static UpdateTestArNameCommand randomUpdateTestArNameCommand() {
        return new UpdateTestArNameCommand(randomTestArName());
    }

    private static CreateTestArCommand randomCreateTestArCommand() {
        return new CreateTestArCommand(randomTestArName());
    }

}