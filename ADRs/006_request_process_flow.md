# Request process flow

## Context

We know that AggregateRoot is the most important concepts in domain model. Nearly all operations in the software are
centered around AggregateRoots. Different types of operations might have their own process flows.

## Decision

We choose to follow a standard way to implement various **request process flows**.

## Implementation

There are mainly 3 ways to interact with the software:

- Send HTTP request to the application
- Scheduled jobs triggered by timers
- Send messages/events to the application, such as via Kafka/MQTT etc.

For HTTP requests, they can be further split into 4 categories:

- HTTP request for creating data
- HTTP request for updating data
- HTTP request for deleting data
- HTTP request for querying data

With the above, we end up with the following types of interactions, let's explain them one by one.

### HTTP request for creating data

Creating data involves 2 major steps: Create and Save. Take "Creating an equipment" as an example, the request process
flow is:

1. Receive the request in the `EquipmentController`, controller calls `EquipmentCommandService`:

```java
    @PostMapping
    public ResponseId createEquipment(@RequestBody @Valid CreateEquipmentCommand command) {
        // In real situations, principal is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Principal principal = TEST_USER_PRINCIPAL;

        return new ResponseId(this.equipmentCommandService.createEquipment(command, principal));
    }
```

2. `EquipmentCommandService` orchestrates the creation process:

```java
    @Transactional
    public String createEquipment(CreateEquipmentCommand command, Principal principal) {
        Equipment equipment = equipmentFactory.create(command.name(), principal);
        equipmentRepository.save(equipment);
        log.info("Created Equipment[{}].", equipment.getId());
        return equipment.getId();
    }
```

3. `EquipmentFactory` is used to create the `Equipment` object. Remember: for code consistency, always use Factory to
   create AggregateRoots:

```java
public class EquipmentFactory {
    public Equipment create(String name, Principal principal) {
        return new Equipment(name, principal);
    }
}
```

4. In the `Equipment` constructor, generate the ID for `Equipment` using `newEquipmentId()`, set data fields, and raise
   `EquipmentCreatedEvent`. After `raiseEvent()` is called, the `EquipmentCreatedEvent` will be send to Kafka
   automatically by the event infrastructure and further actions are required from your side:

```java
    public Equipment(String name, Principal principal) {
        super(newEquipmentId(), principal);
        this.name = name;
        raiseEvent(new EquipmentCreatedEvent(this));
    }

    public static String newEquipmentId() {
        return "EQP" + newSnowflakeId(); // Generate ID in the code
    }
```

5. Call `EquipmentRepository.save()` to save the newly created `Equipment` object:

```java
public interface EquipmentRepository {
    void save(Equipment equipment);
}
```

6. Return the ID of the newly created Equipment object to the caller.

### HTTP request for updating data

Updating data has 3 major steps: (1)Load the AggregateRoot; (2)Call AggregateRoot's business method; (3) Save it back to
database. Take updating `Equipment`'s holder name as an example.

1. The request first arrived at `EquipmentController.updateEquipmentHolder()`:

```java
    @PutMapping("/{equipmentId}/holder")
    public void updateEquipmentHolder(@PathVariable("equipmentId") @NotBlank String equipmentId,
                                      @RequestBody @Valid UpdateEquipmentHolderCommand command) {
        // In real situations, principal is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Principal principal = TEST_USER_PRINCIPAL;

        this.equipmentCommandService.updateEquipmentHolder(equipmentId, command, principal);
    }
```

2. The controller calls `EquipmentCommandService.updateEquipmentHolder()`:

```java
    @Transactional
    public void updateEquipmentHolder(String id, UpdateEquipmentHolderCommand command, Principal principal) {
        Equipment equipment = equipmentRepository.byId(id, principal.getOrgId());
        equipment.updateHolder(command.name());
        equipmentRepository.save(equipment);
        log.info("Updated holder for Equipment[{}].", equipment.getId());
    }
```

3. `EquipmentCommandService` loads the `Equipment` by its ID:

```java
        Equipment equipment = equipmentRepository.byId(id, principal.getOrgId());
```

4. Then call `Equipment`'s business method `Equipment.updateHolder()`:

```java
        equipment.updateHolder(command.name());
```

5. Inside the business method, domain event can be raised according to requirements.
6. Save the updated `Equipment` back into database:

```java
        equipmentRepository.save(equipment);
```

7. No need to return anything from `EquipmentCommandService.updateEquipmentHolder()`.

Sometimes, the whole business logic is not suitable to be put inside AggregateRoot like `Equipment.updateHolder()`. For
such case, we can use a DomainService. For example, when updating `Equipment`'s name, we need to check if the name is
already been occupied, which cannot be fulfilled by `Equipment` itself. Instead of calling `Equipment.updateName()`
directly from `EquipmentCommandService`, `EquipmentDomainService.updateEquipmentName()` is called from
`EquipmentCommandService`:

```java
    @Transactional
    public void updateEquipmentName(String id, UpdateEquipmentNameCommand command, Principal principal) {
        Equipment equipment = equipmentRepository.byId(id, principal.getOrgId());
        equipmentDomainService.updateEquipmentName(equipment, command.name());
        equipmentRepository.save(equipment);
        log.info("Updated name for Equipment[{}].", equipment.getId());
    }
```

Inside `EquipmentDomainService.updateEquipmentName()`, it first checks whether the name is already taken, if not then
update `Equipment`'s name:

```java
    public void updateEquipmentName(Equipment equipment, String newName) {
        if (!Objects.equals(newName, equipment.getName()) &&
            equipmentRepository.existsByName(newName, equipment.getOrgId())) {
            throw new ServiceException(EQUIPMENT_NAME_ALREADY_EXISTS,
                    "Equipment Name Already Exists.",
                    mapOf(AggregateRoot.Fields.id, equipment.getId(), Equipment.Fields.name, newName));
        }

        equipment.updateName(newName);
    }
```

### HTTP request for deleting data

For deleting data, first load the `AggregateRoot` and then delete it. For example, for deleting an `Equipment`

1. Request arrived at `EquipmentController`:

```java
    @DeleteMapping("/{equipmentId}")
    public void deleteEquipment(@PathVariable("equipmentId") @NotBlank String equipmentId) {
        // In real situations, principal is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Principal principal = TEST_USER_PRINCIPAL;

        this.equipmentCommandService.deleteEquipment(equipmentId, principal);
    }

```

2. `EquipmentController` calls `EquipmentCommandService`:

```java
    @Transactional
    public void deleteEquipment(String equipmentId, Principal principal) {
        Equipment equipment = equipmentRepository.byId(equipmentId, principal.getOrgId());
        equipmentRepository.delete(equipment);
        log.info("Deleted Equipment[{}].", equipmentId);
    }
```

3. `EquipmentCommandService` loads the `Equipment`, then call `EquipmentRepository.delete()` to delete it. You might be
   wondering, why we need to first load the `Equipment` into memory then do the deletion. Will it be much simpler to
   directly delete by ID? The reason is that, before deletion, there might be some validations that need to happen, and
   also it might raise domain events. So, in order to ensure such possibilities, the whole `Equipment` object is loaded
   into the memory.
4. When `EquipmentRepository.delete()` is called, it automatically calls `AggregateRoot.onDelete()` which is implemented
   by `Equipment` to raise `EquipmentDeletedEvent`:

```java
    @Override
    public void onDelete() {
        raiseEvent(new EquipmentDeletedEvent(this));
    }
```

### HTTP request for querying data

As we are using [CQRS](./004_use_cqrs.md), querying data can bypass the domain models and talk to database directly. For
example, when querying a list of `Equipment`s:

1. The request hit `EquipmentController`, which further calls `EquipmentQueryService.listEquipments()`:

```java
    @PostMapping("/list")
    public Page<QListedEquipment> listEquipments(@RequestBody @Valid ListEquipmentQuery query,
                                                 @PageableDefault Pageable pageable) {
        // In real situations, principal is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Principal principal = TEST_USER_PRINCIPAL;

        return this.equipmentQueryService.listEquipments(query, pageable, principal);
    }
```

Here Spring's `Pageable` and `Page` should be used for pagination. `EquipmentQueryService` is at the same level with
`EquipmentCommandService`, they both are under the category of `ApplicationService`.

2. `EquipmentQueryService.listEquipments()` uses `MongoTemplate` to query data from database directly, and uses its own
   data model class `QListedEquipment`:

```java
    public Page<QListedEquipment> listEquipments(ListEquipmentQuery listEquipmentQuery, Pageable pageable, Principal principal) {
        Criteria criteria = where(AggregateRoot.Fields.orgId).is(principal.getOrgId());

        if (isNotBlank(listEquipmentQuery.search())) {
            criteria.and(Equipment.Fields.name).regex(listEquipmentQuery.search());
        }
        
        //more code omitted

        Query query = Query.query(criteria);

        long count = mongoTemplate.count(query, Equipment.class);
        if (count == 0) {
            return Page.empty(pageable);
        }

        List<QListedEquipment> devices = mongoTemplate.find(query.with(pageable), QListedEquipment.class, EQUIPMENT_COLLECTION);
        return new PageImpl<>(devices, pageable, count);
    }
```

### Scheduled jobs triggered by timers

First create a scheduler in the `job` package:

```java
public class EquipmentScheduler {
    private final MaintenanceReminderJob maintenanceReminderJob;

    @Scheduled(cron = "0 10 2 1 * ?")
    @SchedulerLock(name = "maintenanceReminderJob")
    public void maintenanceReminderJob() {
        LockAssert.assertLocked();
        this.maintenanceReminderJob.run();
    }
}
```

Then create a job class:

```java
public class MaintenanceReminderJob {

    public void run() {
        log.info("MaintenanceReminderJob started.");

        //do something

        log.info("MaintenanceReminderJob ended.");
    }
}
```

The job class serves the same purpose as `CommandService`, which orchestrates various other components such as
`Repository`, `AggreateRoot` and `Factory` to accomplish a certain process. Hence the job itself should not
contain business logic.

### Consuming events from Kafka

The Kafka event consuming infrastructure is already set up. You only need to do 2 things for consuming events.

1. Make sure the topic is subscribed in `SpringKafkaEventListener` by configuring
   `topics = {KAFKA_DOMAIN_EVENT_TOPIC},`:

```java
public class SpringKafkaEventListener {
    private final EventConsumer eventConsumer;

    @KafkaListener(id = "domain-event-listener",
            groupId = "domain-event-listener",
            topics = {KAFKA_DOMAIN_EVENT_TOPIC},
            concurrency = "3")
    public void listenDomainEvent(DomainEvent event) {
        this.eventConsumer.consumeDomainEvent(event);
    }
}
```

You may add more `@KafkaListener` methods if a different category of events are consumed.

2. Create an EventHandler class that extends `AbstractEventHandler`:

```java
@Slf4j
@Component
@RequiredArgsConstructor
public class EquipmentCreatedEventHandler extends AbstractEventHandler<EquipmentCreatedEvent> {
    private final EquipmentRepository equipmentRepository;

    @Override
    public void handle(EquipmentCreatedEvent event) {
        equipmentRepository.evictCachedEquipmentSummaries(event.getArOrgId());
    }
}
```

The `EventHandler` serves the same purpose as `CommandService`, which orchestrates various other components such as
`Repository`, `AggreateRoot` and `Factory` to accomplish a certain process. Hence the `EventHandler` itself should not
contain business logic.
