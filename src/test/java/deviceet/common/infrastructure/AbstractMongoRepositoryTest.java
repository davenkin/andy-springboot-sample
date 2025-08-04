package deviceet.common.infrastructure;

import deviceet.IntegrationTest;
import deviceet.common.exception.ServiceException;
import deviceet.common.security.Principal;
import deviceet.testar.domain.TestAr;
import deviceet.testar.domain.TestArFactory;
import deviceet.testar.domain.TestArRepository;
import deviceet.testar.domain.event.TestArCreatedEvent;
import deviceet.testar.domain.event.TestArDeletedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static deviceet.TestRandomUtil.randomPrincipal;
import static deviceet.TestRandomUtil.randomTestArName;
import static deviceet.common.event.DomainEventType.TEST_AR_CREATED_EVENT;
import static deviceet.common.event.DomainEventType.TEST_AR_DELETED_EVENT;
import static deviceet.common.exception.ErrorCode.NOT_SAME_ORG;
import static org.junit.jupiter.api.Assertions.*;

class AbstractMongoRepositoryTest extends IntegrationTest {

    @Autowired
    private TestArFactory testArFactory;

    @Autowired
    private TestArRepository testArRepository;

    @Test
    public void should_save_ar() {
        Principal principal = randomPrincipal();
        TestAr testAr = testArFactory.create(randomTestArName(), principal);
        assertEquals(1, testAr.getEvents().size());
        assertInstanceOf(TestArCreatedEvent.class, testAr.getEvents().get(0));
        testArRepository.save(testAr);

        assertNull(testAr.getEvents());

        TestArCreatedEvent createdEvent = latestEventFor(testAr.getId(), TEST_AR_CREATED_EVENT, TestArCreatedEvent.class);
        assertEquals(testAr.getId(), createdEvent.getTestArId());
        TestAr dbAr = testArRepository.byIdOptional(testAr.getId()).get();
        assertNull(dbAr.getEvents());
    }

    @Test
    public void should_save_ars() {
        Principal principal = randomPrincipal();
        TestAr testAr1 = testArFactory.create(randomTestArName(), principal);
        TestAr testAr2 = testArFactory.create(randomTestArName(), principal);

        testArRepository.save(List.of(testAr1, testAr2));

        assertTrue(testArRepository.byIdOptional(testAr1.getId()).isPresent());
        assertTrue(testArRepository.byIdOptional(testAr2.getId()).isPresent());
        TestArCreatedEvent createdEvent1 = latestEventFor(testAr1.getId(), TEST_AR_CREATED_EVENT, TestArCreatedEvent.class);
        assertEquals(testAr1.getId(), createdEvent1.getTestArId());
        TestArCreatedEvent createdEvent2 = latestEventFor(testAr2.getId(), TEST_AR_CREATED_EVENT, TestArCreatedEvent.class);
        assertEquals(testAr2.getId(), createdEvent2.getTestArId());
    }

    @Test
    public void should_throw_exception_if_not_the_same_org() {
        TestAr testAr1 = testArFactory.create(randomTestArName(), randomPrincipal());
        TestAr testAr2 = testArFactory.create(randomTestArName(), randomPrincipal());

        ServiceException exception = assertThrows(ServiceException.class, () -> testArRepository.save(List.of(testAr1, testAr2)));
        assertEquals(NOT_SAME_ORG, exception.getCode());
    }

    @Test
    public void should_delete_ar() {
        Principal principal = randomPrincipal();
        TestAr testAr = testArFactory.create(randomTestArName(), principal);

        testArRepository.save(testAr);
        assertTrue(testArRepository.byIdOptional(testAr.getId()).isPresent());

        testArRepository.delete(testAr);
        assertNull(testAr.getEvents());
        assertFalse(testArRepository.byIdOptional(testAr.getId()).isPresent());
        TestArDeletedEvent deletedEvent = latestEventFor(testAr.getId(), TEST_AR_DELETED_EVENT, TestArDeletedEvent.class);
        assertEquals(testAr.getId(), deletedEvent.getTestArId());
    }

    @Test
    public void should_delete_ars() {
        Principal principal = randomPrincipal();
        TestAr testAr1 = testArFactory.create(randomTestArName(), principal);
        TestAr testAr2 = testArFactory.create(randomTestArName(), principal);

        testArRepository.save(List.of(testAr1, testAr2));

        assertTrue(testArRepository.byIdOptional(testAr1.getId()).isPresent());
        assertTrue(testArRepository.byIdOptional(testAr2.getId()).isPresent());

        testArRepository.delete(List.of(testAr1, testAr2));
        assertNull(testAr1.getEvents());
        assertNull(testAr2.getEvents());
        assertFalse(testArRepository.byIdOptional(testAr1.getId()).isPresent());
        assertFalse(testArRepository.byIdOptional(testAr2.getId()).isPresent());

        TestArDeletedEvent deletedEvent1 = latestEventFor(testAr1.getId(), TEST_AR_DELETED_EVENT, TestArDeletedEvent.class);
        assertEquals(testAr1.getId(), deletedEvent1.getTestArId());
        TestArDeletedEvent deletedEvent2 = latestEventFor(testAr2.getId(), TEST_AR_DELETED_EVENT, TestArDeletedEvent.class);
        assertEquals(testAr2.getId(), deletedEvent2.getTestArId());
    }

    @Test
    public void should_throw_exception_if_not_the_same_org_for_delete() {
        TestAr testAr1 = testArFactory.create(randomTestArName(), randomPrincipal());
        TestAr testAr2 = testArFactory.create(randomTestArName(), randomPrincipal());

        ServiceException exception = assertThrows(ServiceException.class, () -> testArRepository.delete(List.of(testAr1, testAr2)));
        assertEquals(NOT_SAME_ORG, exception.getCode());
    }

    @Test
    public void should_fetch_ar_by_id() {
        Principal principal = randomPrincipal();
        TestAr testAr = testArFactory.create(randomTestArName(), principal);
        assertFalse(testArRepository.exists(testAr.getId(), principal.getOrgId()));

        testArRepository.save(testAr);

        TestAr dbAr = testArRepository.byId(testAr.getId(), principal.getOrgId());
        assertEquals(testAr.getId(), dbAr.getId());

        TestAr dbAr2 = testArRepository.byIdOptional(testAr.getId(), principal.getOrgId()).get();
        assertEquals(testAr.getId(), dbAr2.getId());

        TestAr dbAr3 = testArRepository.byIdOptional(testAr.getId()).get();
        assertEquals(testAr.getId(), dbAr3.getId());

        assertTrue(testArRepository.exists(testAr.getId(), principal.getOrgId()));
    }

}