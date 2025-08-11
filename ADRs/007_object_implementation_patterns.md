# Object implementation patterns

## Context

There are various types of objects in software, such as Controller, Domain Object, Factory, Repository, Value Object
etc. Their responsibilities and characteristics differ, but the same type of object share something in common and it's
important that we keep our coding practices consistent within the same type of objects.

## Decision

For the same type of objects, we follow the same implementation patterns.

## Implementation

### Aggregate Root

- Aggregate Roots are the most important types of objects in your software, they contain your core domain logic, they
  are the sole reason your software exists
- Aggregate Root should extend [AggregateRoot](../src/main/java/deviceet/common/model/AggregateRoot.java) base
  class
- All changes to the internal state of Aggregate Roots should go via the public methods of Aggregate Roots
- Every public methods in Aggregate Root should leave the Aggregate Root in valid state according to business rules
- Aggregate Root should use meaningful constructors to create itself
- For code consistency, always use Factory to create Aggregate Root
- Aggregate Root should not have builder method because builder method can easily results in invalid object
- Aggregate Root should have a globally unique ID and this ID should be generate from the code but not database
- Aggregate Root should have meaningful business methods for changing its own state. Every business method should ensure
  the object is always in valid state by applying business rules. Business methods might raise domain events after state
  changed.
- Aggregate Root has the following class level annotations:
    - `@Slf4j`: for log
    - `@Getter`: for retrieving data (actually getters are quite bad as it violates information hiding principle, but
      for convenience let's keep them)
    - `@FieldNameConstants`: for access filed names in situations like accessing MongoDB
    - `@TypeAlias(EQUIPMENT_COLLECTION)`: use a explict type alias, otherwise the FQCN will be used by Spring Data
      MongoDB which does not survive changing package locations
    - `@Document(EQUIPMENT_COLLECTION)`: for MongoDB collection
    - `@NoArgsConstructor(access = PRIVATE)`: for Jackson deserialization
- Aggregate Root should not be annotated with `@Setter`, `@Builder` or  `@Data`

Example [Equipment](../src/test/java/deviceet/sample/equipment/domain/Equipment.java):

```java
@Slf4j
@Getter
@FieldNameConstants // For access field names
@TypeAlias(EQUIPMENT_COLLECTION) // Use a explict type alias
@Document(EQUIPMENT_COLLECTION) // Configure MongoDB collection name
@NoArgsConstructor(access = PRIVATE) // For Jackson deserialization
public class Equipment extends AggregateRoot {
    public final static String EQUIPMENT_COLLECTION = "equipment";
    private String name;
    private EquipmentStatus status;
    private String holder;
    private long maintenanceRecordCount;

    public Equipment(String name, Principal principal) { // Explict contructors
        super(newEquipmentId(), principal);
        this.name = name;
        raiseEvent(new EquipmentCreatedEvent(this)); // Raise domain event
    }

    public static String newEquipmentId() {
        return "EQP" + newSnowflakeId(); // Generate ID in the code
    }

    public void updateName(String newName) { // Business method
        if (Objects.equals(newName, this.name)) { // Apply business rules
            return;
        }
        this.name = newName; // Update object state 
        raiseEvent(new EquipmentNameUpdatedEvent(name, this)); // Raise domain event
    }
}
```

### Repository

- Repository abstracts database interactions for accessing Aggregate Roots
- Every Aggregate Root has its own Repository class
- Repositories should first have an interface class and then a concrete implementation class
  Example for Repository
  interface [EquipmentRepository](../src/test/java/deviceet/sample/equipment/domain/EquipmentRepository.java):

```java
public interface EquipmentRepository {
    void save(Equipment equipment);

    void save(List<Equipment> equipments);

    void delete(Equipment equipment);
    
    // more code omitted
}
```

Example for Repository
implementation[MongoEquipmentRepository](../src/test/java/deviceet/sample/equipment/infrastructure/MongoEquipmentRepository.java):

```java
@Repository
@RequiredArgsConstructor
public class MongoEquipmentRepository extends AbstractMongoRepository<Equipment> implements EquipmentRepository {
    private final CachedMongoEquipmentRepository cachedMongoEquipmentRepository;

    @Override
    public List<EquipmentSummary> cachedEquipmentSummaries(String orgId) {
        return cachedMongoEquipmentRepository.cachedEquipmentSummaries(orgId).summaries();
    }
    // more code ommited
}
```

- All Repository implementation should
  extend [AbstractMongoRepository](../src/main/java/deviceet/common/infrastructure/AbstractMongoRepository.java)
- For cache, the Repository implementation can reference a cache Repository, the cache Repository also extends
  `AbstractMongoRepository`

Example of cache
Repository [CachedMongoEquipmentRepository](../src/test/java/deviceet/sample/equipment/infrastructure/CachedMongoEquipmentRepository.java):

```java
@Slf4j
@Repository
@RequiredArgsConstructor
public class CachedMongoEquipmentRepository extends AbstractMongoRepository<Equipment> {
    private static final String ORG_EQUIPMENT_CACHE = "ORG_EQUIPMENTS";

    @Cacheable(value = ORG_EQUIPMENT_CACHE, key = "#orgId")
    public CachedOrgEquipmentSummaries cachedEquipmentSummaries(String orgId) {
        requireNonBlank(orgId, "orgId must not be blank.");

        Query query = query(where(AggregateRoot.Fields.orgId).is(orgId)).with(by(ASC, createdAt));
        query.fields().include(AggregateRoot.Fields.orgId, Equipment.Fields.name, Equipment.Fields.status);
        return new CachedOrgEquipmentSummaries(mongoTemplate.find(query, EquipmentSummary.class, EQUIPMENT_COLLECTION));
    }
}
```

### Controller

- Controller should be very thin by offloading the work to CommandService for writing data and QueryService for reading
  data
- Controller classes should be annotated with `@Validated` to enable request validation
- Request objects in method parameters should be annotated with `@Valid` to enable request validation
- Controller ensures a `Principal` is fetched/created from the reqeust and passed to CommandService or QueryService
- Controller should follow REST principles on naming URLs and choosing HTTP methods

Example [EquipmentController](../src/test/java/deviceet/sample/equipment/controller/EquipmentController.java):

```java
@Validated // To enable request validation
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/equipments")
public class EquipmentController {
    private final EquipmentCommandService equipmentCommandService;
    private final EquipmentQueryService equipmentQueryService;

    @PostMapping
    public ResponseId createEquipment(@RequestBody @Valid CreateEquipmentCommand command) {
        // In real situations, principal is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Principal principal = TEST_USER_PRINCIPAL;

        return new ResponseId(this.equipmentCommandService.createEquipment(command, principal));
    }
}
```

### CommandService

- CommandService serves as the facade for the domain model
- Every public method in CommandService should represent a use case, and should be annotated with `@Transactional`
- Methods in CommandService usually accepts a request Command class as parameter, as well as a `Principal`
- CommandService should not contain business logic
- Please follow [requst process flow](./006_request_process_flow.md) on how to implement CommandServices

Example [EquipmentCommandService](../src/test/java/deviceet/sample/equipment/command/EquipmentCommandService.java):

```java
@Slf4j
@Component
@RequiredArgsConstructor
public class EquipmentCommandService {
    private final EquipmentRepository equipmentRepository;
    private final EquipmentFactory equipmentFactory;
    private final EquipmentDomainService equipmentDomainService;

    @Transactional
    public String createEquipment(CreateEquipmentCommand command, Principal principal) {
        Equipment equipment = equipmentFactory.create(command.name(), principal);
        equipmentRepository.save(equipment);
        log.info("Created Equipment[{}].", equipment.getId());
        return equipment.getId();
    }
}
```

### Command

- Command objects are request objects that instructs the software to change its data state
- Command should be modeled as Java Record
- Command can be annotated with `@Builder` for testing purpose
- Command object should use JSR-303 annotations  (such as `@NotNull`, `@Max` and `@Pattern`) for data validation

Example [CreateMaintenanceRecordCommand](../src/test/java/deviceet/sample/maintenance/command/CreateMaintenanceRecordCommand.java):

```java
@Builder
public record CreateMaintenanceRecordCommand(
        @NotBlank String equipmentId,
        @NotBlank @Size(max = 1000) String description,
        @NotNull EquipmentStatus status
) {
}
```

### Domain Event

- All Domain Events should extend [DomainEvent](../src/main/java/deviceet/common/event/DomainEvent.java)
- Domain Events should be immutable as it represent something already happened which cannot not be changed
- Every Domain Event should register its own type
  inside [DomainEventType](../src/main/java/deviceet/common/event/DomainEventType.java)
- Domain Event should hold enough context data about what happened, but not the whole Aggregate Root or unrelated data
  for this event
- Domain Event should not be annotated with `@Setter`, `@Builder` or  `@Data`
- Domain Events are only raised from Aggregate Roots by using `AggregateRoot.raiseEvent()` method
- Domain Event has the following class level annotations:
    - `@Getter`: for retrieving data (actually getters are quite bad as it violates information hiding principle, but
      for convenience let's keep them)
    - `@TypeAlias(MAINTENANCE_RECORD_CREATED_EVENT)`: use a explict type alias, otherwise the FQCN will be used by
      Spring Data MongoDB which does not survive changing package locations
    - `@NoArgsConstructor(access = PRIVATE)`: for Jackson deserialization

Example [MaintenanceRecordCreatedEvent](../src/test/java/deviceet/sample/maintenance/domain/event/MaintenanceRecordCreatedEvent.java):

```java
@Getter
@TypeAlias("MAINTENANCE_RECORD_CREATED_EVENT")
@NoArgsConstructor(access = PRIVATE)
public class MaintenanceRecordCreatedEvent extends DomainEvent {
    private String maintenanceRecordId;
    private String equipmentId;
    private String equipmentName;

    public MaintenanceRecordCreatedEvent(MaintenanceRecord maintenanceRecord) {
        super(MAINTENANCE_RECORD_CREATED_EVENT, maintenanceRecord);
        this.maintenanceRecordId = maintenanceRecord.getId();
        this.equipmentId = maintenanceRecord.getEquipmentId();
        this.equipmentName = maintenanceRecord.getEquipmentName();
    }
}
```

### EventHandler

- All EventHandlers should
  extend [AbstractEventHandler](../src/main/java/deviceet/common/event/consume/AbstractEventHandler.java)
- You may choose to override `AbstractEventHandler`'s `isIdempotent()`, `isTransactional()` and `priority()` for
  specific purposes
- EventHandler serves a similar purpose as CommandService in that they both result in data state changes in the
  software, and they both are facade which orchestrate other components to work but does not contain business logic by
  itself
- EventHandler can use `ExceptionSwallowRunner` to run multiple independent operations, in which exception raised in one
  operation does not affect later operations

Example [EquipmentDeletedEventEventHandler](../src/test/java/deviceet/sample/equipment/eventhandler/EquipmentDeletedEventEventHandler.java):

```java
@Slf4j
@Component
@RequiredArgsConstructor
public class EquipmentDeletedEventEventHandler extends AbstractEventHandler<EquipmentDeletedEvent> {
    private final DeleteAllMaintenanceRecordsUnderEquipmentTask deleteAllMaintenanceRecordsUnderEquipmentTask;
    private final EquipmentRepository equipmentRepository;

    @Override
    public void handle(EquipmentDeletedEvent event) {
        ExceptionSwallowRunner.run(() -> {
            equipmentRepository.evictCachedEquipmentSummaries(event.getArOrgId());
        });

        ExceptionSwallowRunner.run(() -> deleteAllMaintenanceRecordsUnderEquipmentTask.run(event.getEquipmentId()));
    }

    @Override
    public boolean isIdempotent() {
        return true;// This handler can run multiple times safely
    }

    @Override
    public boolean isTransactional() {
        return false; // Better not be transactional as it deletes multiple records which can exceed Mongo transaction restrictions
    }
}
```

### Factory

- Factory is used to create Aggregate Root
- In Factories, before calling Aggregate Roots's constructors, there usually exists some business validations
- If no business validation is required, the Factory can be as simple as just call Aggregate Roots's constructors, but
  for consistency, let's always use Factory to create Aggregate Roots.
- Use Factory to create Aggregate Roots makes our code more explict as the creation of Aggregate Roots is an important
  moment in software

Example [MaintenanceRecordFactory](../src/test/java/deviceet/sample/maintenance/domain/MaintenanceRecordFactory.java):

```java
@Component
@RequiredArgsConstructor
public class MaintenanceRecordFactory {

    public MaintenanceRecord create(Equipment equipment,
                                    EquipmentStatus status,
                                    String description,
                                    Principal principal) {
        return new MaintenanceRecord(equipment.getId(), equipment.getName(), status, description, principal);
    }
}
```

### Task

- Tasks represents a standalone operation that usually involves multiple database rows(documents)
- Tasks is like DomainService, but for convenience it can access database directly using `MongoTemplate`
- Tasks are usually called by EventHandlers

Example [SyncEquipmentNameToMaintenanceRecordsTask](../src/test/java/deviceet/sample/equipment/domain/task/SyncEquipmentNameToMaintenanceRecordsTask.java):

```java
@Slf4j
@Component
@RequiredArgsConstructor
public class SyncEquipmentNameToMaintenanceRecordsTask {
    private final MongoTemplate mongoTemplate;
    private final EquipmentRepository equipmentRepository;

    public void run(String equipmentId) {
        equipmentRepository.byIdOptional(equipmentId).ifPresent(equipment -> {
            Query query = new Query(where(MaintenanceRecord.Fields.equipmentId).is(equipmentId));
            Update update = new Update().set(MaintenanceRecord.Fields.equipmentName, equipment.getName());
            mongoTemplate.updateMulti(query, update, MaintenanceRecord.class);
            log.info("Synced equipment[{}] name to all maintenance records.", equipment.getId());
        });
    }
}
```

