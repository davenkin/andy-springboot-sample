# Request process flow

## Context

We know that Aggregate Root is the most important concepts in domain model. Nearly all operations in the software are
centered around Aggregate Roots. Different types of operations might have their own process flows.

## Decision

We choose to follow a standard way to implement various **request process flows**.

## Implementation

There are mainly 3 ways to interact with the software:

- Send HTTP request to the application
- Scheduled jobs triggered by timers
- Consuming events from Kafka

For HTTP requests, they can be further split into multiple sub-categories.

Given above, we have the following process flows:

- [HTTP request for creating data](#http-request-for-creating-data)
- [HTTP request for updating data](#http-request-for-updating-data)
- [HTTP request for deleting data](#http-request-for-deleting-data)
- [HTTP request for querying data](#http-request-for-querying-data)
- [Scheduled jobs triggered by timers](#scheduled-jobs-triggered-by-timers)
- [Consuming events from Kafka](#consuming-events-from-kafka)

### HTTP request for creating data

Creating data involves 2 major steps: Create and Save. Take "Creating an equipment" as an example, the request process
flow is:

1. Receive the request in the `EquipmentController`, controller calls `EquipmentCommandService`:

```java
    @PostMapping
    public ResponseId createEquipment(@RequestBody @Valid CreateEquipmentCommand command) {
        // In real situations, operator is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Operator operator = SAMPLE_USER_OPERATOR;

        return new ResponseId(this.equipmentCommandService.createEquipment(command, operator));
    }
```

2. `EquipmentCommandService` orchestrates the creation process:

```java
    @Transactional
    public String createEquipment(CreateEquipmentCommand command, Operator operator) {
        Equipment equipment = equipmentFactory.create(command.name(), operator);
        equipmentRepository.save(equipment);
        log.info("Created Equipment[{}].", equipment.getId());
        return equipment.getId();
    }
```

3. `EquipmentFactory` is used to create the `Equipment` object. Remember: for code consistency, always use Factory to
   create Aggregate Roots:

```java
public class EquipmentFactory {
    public Equipment create(String name, Operator operator) {
        return new Equipment(name, operator);
    }
}
```

4. In the `Equipment` constructor, generate the ID for `Equipment` using `newEquipmentId()`, set data fields, and raise
   `EquipmentCreatedEvent`. After `raiseEvent()` is called, the `EquipmentCreatedEvent` will be send to Kafka
   automatically by the event infrastructure and no further actions are required from your side:

```java
    public Equipment(String name, Operator operator) {
        super(newEquipmentId(), operator);
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

Updating data has 3 major steps: (1)Load the Aggregate Root; (2)Call Aggregate Root's business method; (3) Save it back
to
database. Take "updating `Equipment`'s holder name" as an example.

1. The request first arrives at `EquipmentController.updateEquipmentHolder()`:

```java
    @PutMapping("/{equipmentId}/holder")
    public void updateEquipmentHolder(@PathVariable("equipmentId") @NotBlank String equipmentId,
                                      @RequestBody @Valid UpdateEquipmentHolderCommand command) {
        // In real situations, operator is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Operator operator = SAMPLE_USER_OPERATOR;

        this.equipmentCommandService.updateEquipmentHolder(equipmentId, command, operator);
    }
```

2. The controller calls `EquipmentCommandService.updateEquipmentHolder()`:

```java
    @Transactional
    public void updateEquipmentHolder(String id, UpdateEquipmentHolderCommand command, Operator operator) {
        Equipment equipment = equipmentRepository.byId(id, operator.getOrgId());
        equipment.updateHolder(command.name());
        equipmentRepository.save(equipment);
        log.info("Updated holder for Equipment[{}].", equipment.getId());
    }
```

3. `EquipmentCommandService` loads the `Equipment` by its ID:

```java
        Equipment equipment = equipmentRepository.byId(id, operator.getOrgId());
```

4. Then call `Equipment`'s business method `Equipment.updateHolder()`:

```java
        equipment.updateHolder(command.name());
```

5. Inside the business method, Domain Event can be raised according to requirements.
6. Save the updated `Equipment` back into database:

```java
        equipmentRepository.save(equipment);
```

7. No need to return anything from `EquipmentCommandService.updateEquipmentHolder()`.

Sometimes, the whole business logic is not suitable to be put inside Aggregate Root like `Equipment.updateHolder()`. For
such cases, we can use DomainServices. For example, when updating `Equipment`'s name, we need to check if the name is
already been occupied, which cannot be fulfilled by `Equipment` itself. Instead of calling `Equipment.updateName()`
directly from `EquipmentCommandService`, `EquipmentDomainService.updateEquipmentName()` is called from
`EquipmentCommandService`:

```java
    @Transactional
    public void updateEquipmentName(String id, UpdateEquipmentNameCommand command, Operator operator) {
        Equipment equipment = equipmentRepository.byId(id, operator.getOrgId());
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

1. Request arrives at `EquipmentController`:

```java
    @DeleteMapping("/{equipmentId}")
    public void deleteEquipment(@PathVariable("equipmentId") @NotBlank String equipmentId) {
        // In real situations, operator is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Operator operator = SAMPLE_USER_OPERATOR;

        this.equipmentCommandService.deleteEquipment(equipmentId, operator);
    }

```

2. `EquipmentController` calls `EquipmentCommandService`:

```java
    @Transactional
    public void deleteEquipment(String equipmentId, Operator operator) {
        Equipment equipment = equipmentRepository.byId(equipmentId, operator.getOrgId());
        equipmentRepository.delete(equipment);
        log.info("Deleted Equipment[{}].", equipmentId);
    }
```

3. `EquipmentCommandService` loads the `Equipment`, then call `EquipmentRepository.delete()` to delete it. You might be
   wondering, why we need to first load the `Equipment` into memory then do the deletion. Will it be much simpler to
   directly delete by ID? The reason is that, before deletion, there might be some validations that need to happen, and
   also it might raise Domain Events. So, in order to ensure such possibilities, the whole `Equipment` object is loaded
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

1. The request hits `EquipmentController`, which further calls `EquipmentQueryService.listEquipments()`:

```java
    @PostMapping("/list")
    public Page<QListedEquipment> listEquipments(@RequestBody @Valid ListEquipmentQuery query,
                                                 @PageableDefault Pageable pageable) {
        // In real situations, operator is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Operator operator = SAMPLE_USER_OPERATOR;

        return this.equipmentQueryService.listEquipments(query, pageable, operator);
    }
```

Here Spring's `Pageable` and `Page` should be used for pagination. `EquipmentQueryService` is at the same level with
`EquipmentCommandService`, they both are under the category of `ApplicationService`.

2. `EquipmentQueryService.listEquipments()` uses `MongoTemplate` to query data from database directly, and uses its own
   query model `QListedEquipment`:

```java
    public Page<QListedEquipment> listEquipments(ListEquipmentQuery listEquipmentQuery, Pageable pageable, Operator operator) {
        Criteria criteria = where(AggregateRoot.Fields.orgId).is(operator.getOrgId());

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
`Repository`, `AggreateRoot` and `Factory`. Hence the job itself should not
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
`Repository`, `AggreateRoot` and `Factory`. Hence the `EventHandler` itself should not
contain business logic.
