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

