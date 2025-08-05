package deviceet.common.infrastructure;

import deviceet.IntegrationTest;
import deviceet.business.testar.domain.TestAr;
import deviceet.business.testar.domain.TestArFactory;
import deviceet.business.testar.domain.TestArRepository;
import deviceet.business.testar.domain.event.TestArCreatedEvent;
import deviceet.business.testar.domain.event.TestArDeletedEvent;
import deviceet.common.exception.ServiceException;
import deviceet.common.security.Principal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static deviceet.TestRandomUtils.randomPrincipal;
import static deviceet.TestRandomUtils.randomTestArName;
import static deviceet.common.event.DomainEventType.TEST_AR_CREATED_EVENT;
import static deviceet.common.event.DomainEventType.TEST_AR_DELETED_EVENT;
import static deviceet.common.exception.ErrorCode.AR_NOT_FOUND;
import static deviceet.common.exception.ErrorCode.NOT_SAME_ORG;
import static org.apache.commons.lang3.RandomStringUtils.secure;
import static org.junit.jupiter.api.Assertions.*;

class AbstractMongoRepositoryIntegrationTest extends IntegrationTest {

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

        assertEquals(testAr.getId(), testArRepository.byId(testAr.getId()).getId());
        assertEquals(testAr.getId(), testArRepository.byId(testAr.getId(), principal.getOrgId()).getId());
        assertEquals(testAr.getId(), testArRepository.byIdOptional(testAr.getId(), principal.getOrgId()).get().getId());
        assertEquals(testAr.getId(), testArRepository.byIdOptional(testAr.getId()).get().getId());
        assertTrue(testArRepository.exists(testAr.getId(), principal.getOrgId()));

        assertEquals(AR_NOT_FOUND, assertThrows(ServiceException.class, () -> testArRepository.byId(secure().nextAlphanumeric(5), secure().nextAlphanumeric(5))).getCode());
        assertEquals(AR_NOT_FOUND, assertThrows(ServiceException.class, () -> testArRepository.byId(secure().nextAlphanumeric(5))).getCode());

        assertFalse(testArRepository.byIdOptional(secure().nextAlphanumeric(5)).isPresent());
        assertFalse(testArRepository.byIdOptional(secure().nextAlphanumeric(5), secure().nextAlphanumeric(5)).isPresent());
    }

}