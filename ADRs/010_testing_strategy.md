# Testing strategy

## Context

Backend developers usually write both unit tests and integration tests. According
to [Testing Pyramid](https://martinfowler.com/bliki/TestPyramid.html), unit tests
constitute the base of the pyramid, while integration tests are at a level higher. The goal is to have a large number of
unit tests and a smaller number of integration tests. But in our case, we find that unit tests can be too fragile and
require frequent updates when the code changes, and it gives us less confidence then integration tests, more detail can
be found [here](https://web.dev/articles/ta-strategies).

## Decision

We choose to focus more on integration tests than unit tests.

We write integration tests for:

- CommandService,
  e.g. [EquipmentCommandServiceIntegrationTest](../src/test/java/deviceet/sample/equipment/command/EquipmentCommandServiceIntegrationTest.java)
- QueryService,
  e.g. [EquipmentQueryServiceIntegrationTest](../src/test/java/deviceet/sample/equipment/query/EquipmentQueryServiceIntegrationTest.java)
- DomainEventHandler,
  e.g. [EquipmentDeletedEventEventHandlerIntegrationTest](../src/test/java/deviceet/sample/equipment/eventhandler/EquipmentDeletedEventEventHandlerIntegrationTest.java)
- Job,
  e.g. [RemoveOldMaintenanceRecordsJobIntegrationTest](../src/test/java/deviceet/sample/maintenance/job/RemoveOldMaintenanceRecordsJobIntegrationTest.java)

We write unit tests for:

- Aggregate Roots, e.g. [EquipmentTest](../src/test/java/deviceet/sample/equipment/domain/EquipmentTest.java)
- Other domain models under `domain` package,
  e.g. [EquipmentDomainServiceTest](../src/test/java/deviceet/sample/equipment/domain/EquipmentDomainServiceTest.java)
- Actually these objects are already covered in integration tests, but integration tests can be quite heavy, so the plan
  is to let integration tests cover the main flow and unit tests cover other corner cases

We don't write tests for:

- Controller: controllers are very thin but requires a heavy set up for testing
- Repository: repositories talks to database, better to cover it in integration tests

## Implementation

#### Integration Tests

- All integration tests should extend [IntegrationTest](../src/test/java/deviceet/IntegrationTest.java):

```java
@Slf4j
@ActiveProfiles(IT_PROFILE)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class IntegrationTest {
    private static RedisServer redisServer;
}
```

The `IntegrationTest` is annotated with `@SpringBootTest`, instructing the test to load the whole Spring application
context, hence creating an integration test environment.

It's also annotated with `@ActiveProfiles(IT_PROFILE)` which
uses [application-it.yaml](../src/test/resources/application-it.yaml) for Spring configuration.

- When write your own integration test class, you only need to extend `IntegrationTest`:

```java
class EquipmentCommandServiceIntegrationTest extends IntegrationTest {
    @Autowired
    private EquipmentCommandService equipmentCommandService;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Test
    void should_create_equipment() {}
}
```

You can use `@Autowire` to get an instance of the bean that should be tested, or whatever beans that you require to
assist you testing.

- We want developers to run the tests without any local setup, hence:
    - Kafka is disabled because it's asynchronous and hard to manage
    - Embedded Redis server is used: `com.github.codemonstur:embedded-redis`
    - Embedded MongoDB server is used: ``de.flapdoodle.embed:de.flapdoodle.embed.mongo.spring3x``
    - All external HTTP services should be mocked
    - Scheduled jobs are disabled

- As Kafka is disabled, you will need to call EventHandlers' `handle()` methods explicitly to ensure the processing of
  events.
- As [Transactional Outbox](https://microservices.io/patterns/data/transactional-outbox.html) pattern is used, the
  Domain Events will firstly be stored into database and then publish, you may use `IntegrationTest.latestEventFor()` to
  verify the existence of events:

```java
    @Test
    void should_create_equipment() {
        Principal principal = randomUserPrincipal();

        CreateEquipmentCommand createEquipmentCommand = randomCreateEquipmentCommand();
        String equipmentId = equipmentCommandService.createEquipment(createEquipmentCommand, principal);

        Equipment equipment = equipmentRepository.byId(equipmentId);
        assertEquals(createEquipmentCommand.name(), equipment.getName());
        assertEquals(principal.getOrgId(), equipment.getOrgId());

        // Verify the existence of Domain Events in database
        EquipmentCreatedEvent equipmentCreatedEvent = latestEventFor(equipmentId, EQUIPMENT_CREATED_EVENT, EquipmentCreatedEvent.class);
        assertEquals(equipmentId, equipmentCreatedEvent.getEquipmentId());
    }
```

- In order to [enhance testing performance](https://www.baeldung.com/spring-tests), please use as less mocks as possible

### Unit Tests

- For unit tests without mocks, it's quite straight forward. Unit tests for Aggregate Roots fall under this category.

```java
class EquipmentTest {
    @Test
    void shouldCreateEquipment() {
        Principal principal = RandomTestUtils.randomUserPrincipal();
        Equipment equipment = new Equipment("name", principal);
        assertEquals("name", equipment.getName());
        assertEquals(1, equipment.getEvents().size());
        assertTrue(equipment.getEvents().stream()
                .anyMatch(domainEvent -> domainEvent.getType() == EQUIPMENT_CREATED_EVENT));
    }
}
```

- For unit tests with mocks, use `@Mock` and `@InjectMocks`, together with `@ExtendWith(MockitoExtension.class)`, to
  simplify the mocking setup:

```java
@ExtendWith(MockitoExtension.class)
class EquipmentDomainServiceTest {

    @Mock
    private EquipmentRepository equipmentRepository;

    @InjectMocks
    private EquipmentDomainService equipmentDomainService;

    @Test
    void shouldUpdateName() {
        Mockito.when(equipmentRepository.existsByName(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        Equipment equipment = new Equipment("name", randomUserPrincipal());

        equipmentDomainService.updateEquipmentName(equipment, "newName");

        assertEquals("newName", equipment.getName());
    }
}
```


