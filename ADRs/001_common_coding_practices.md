# Common coding practices

- Do not rely on database to generate IDs, instead, generate IDs within the code
  using [SnowflakeIdGenerator.newSnowflakeId()](../src/main/java/deviceet/common/util/SnowflakeIdGenerator.java). This
  means when the object is created,
  its ID should already been generated. Reason: This decouples the code from database implementations and also makes
  testing much easier.

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

- Prefer using Java Record over Lombok for value objects. Reason: Records are Java's built in support, they are more
  concise
  and embodies common best practices like immutability.

    ```java
    public record QDetailedEquipment(
        String id,
        String orgId,
        String name,
        EquipmentStatus status,
        Instant createdAt,
        String createdBy) {}
  ```
- Never use Lombok's `@Setter` and `@Data`(which implicitly creates setters). Reason: Setters are bad as they break the
  principles of cohesion and information hiding. Also, objects with setters are just data containers like C's struct,
  they does not convey any business intent, making the code hard to read and comprehend.
- Always use `ServiceException` for raising exceptions, don't create your own exception classes. Reason: The
  `ServiceException` is a flat exception model that makes exception modeling much easier than hierarchical exceptions.

  ```java
          throw new ServiceException(EQUIPMENT_NAME_ALREADY_EXISTS,
                  "Equipment Name Already Exists.",
                  mapOf(AggregateRoot.Fields.id, equipment.getId(), Equipment.Fields.name, newName));
  ```

- Use Spring Data's default pagination mechanism. This means the following query parameters should be used:
    - `page`: the zero-based page index
    - `size`: the page size
    - `sort`: for sorting, format is `sort=abc,desc`, where `abc` is the field to be sorted, `desc` means descending and
      `asc` means ascending.
    - Also the following configuration should be applied for responding a stable `Page` object.

  ```java
  @EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
  public class CommonConfiguration {}
  ```
  Example with `Pageable` and `Page`(always use `@PageableDefault` as well):
  ```java
    @PostMapping("/list")
    public Page<QListedEquipment> listEquipments(@RequestBody @Valid ListEquipmentQuery query,
                                                 @PageableDefault Pageable pageable) {
        // In real situations, principal is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Principal principal = SAMPLE_USER_PRINCIPAL;

        return this.equipmentQueryService.listEquipments(query, pageable, principal);
    }
  ```

- Use Java 8's `Instant` to represent timestamp, don't use `OffsetDateTime` or `ZonedDateTime`. Reason: `Instant` is
  designed for such purpose, there is no point in storing timezone information inside a timestamp.
- Set application default timezone to 'UTC' explicitly by `TimeZone.setDefault(TimeZone.getTimeZone("UTC"))`. Reason: A
  unified default timezone makes things much easier.

```java
@SpringBootApplication
public class SpringBootWebApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC")); // Set default timezone to 'UTC'
        SpringApplication.run(SpringBootWebApplication.class, args);
    }
}
```

- Use Lombok's `@FieldNameConstants` to access objects' field names. For example, when accessing MongoDB, filed names
  are usually needed.

```java
@FieldNameConstants
public class Equipment extends AggregateRoot {}
```

```java
if (listEquipmentQuery.status() != null) {

   // Use "Equipment.Fields.status" to access Equipment's "status" field
   criteria.and(Equipment.Fields.status).is(listEquipmentQuery.status());
}
```

- Do not
  use [Spring Data Repository](https://docs.spring.io/spring-data/commons/reference/repositories/query-methods-details.html).
  Reason: Spring Data's auto generated repository query method names can be very long and hard to read, also it cannot
  survive code refactoring. Instead, implement your own repository classes which
  extends [AbstractMongoRepository](../src/main/java/deviceet/common/infrastructure/AbstractMongoRepository.java), this
  gives you more freedom.

```java
@Repository
@RequiredArgsConstructor
public class MongoEquipmentRepository extends AbstractMongoRepository<Equipment> implements EquipmentRepository {}
```

- Use a single instance of `ObjectMapper` across the whole application as much as possible. Reason: A single
  `ObjectMapper` behaves
  the same for all scenarios. The single `ObejctMapper` is already configured
  inside [CommonConfiguration](../src/main/java/deviceet/common/configuration/CommonConfiguration.java).

```java
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomizer() {
        return builder -> {
            builder.visibility(ALL, ANY)// Make Jackson deal with fields directly without needing setter/getters
                    .featuresToDisable(WRITE_DATES_AS_TIMESTAMPS,
                            WRITE_DURATIONS_AS_TIMESTAMPS,
                            FAIL_ON_UNKNOWN_PROPERTIES);
        };
    }
```

In this `ObjectMapper`, `builder.visibility(ALL, ANY)` is used to enable direct field access, which means there is no
need to
expose getters/setters.

- Always enable transaction in CommandServices by using `@Transactional`.

```java
    @Transactional
    public String createEquipment(CreateEquipmentCommand command, Principal principal) {
        Equipment equipment = equipmentFactory.create(command.name(), principal);
        equipmentRepository.save(equipment);
        log.info("Created Equipment[{}].", equipment.getId());
        return equipment.getId();
    }
```

- If distributed lock is required, used
  Shedlock's [LockingTaskExecutor](../src/main/java/deviceet/common/configuration/DistributedLockConfiguration.java).
- Use HTTP POST for controller methods that return a list of objects, and put all query fields into a `Query` object
  even if there is only one field. Reason: a `Query` object wraps multiple fields together that's easy to pass around.
  Example: use [ListEquipmentQuery](../src/test/java/deviceet/sample/equipment/query/ListEquipmentQuery.java) to query
  multiple equipments.

```java
@Builder
public record ListEquipmentQuery(String search, EquipmentStatus status) {
}
```

The controller receives a `ListEquipmentQuery` object along with a `Pageable` object:

```java
    @PostMapping("/list")
    public Page<QListedEquipment> listEquipments(@RequestBody @Valid ListEquipmentQuery query,
                                                 @PageableDefault Pageable pageable) {
        // In real situations, principal is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Principal principal = SAMPLE_USER_PRINCIPAL;

        return this.equipmentQueryService.listEquipments(query, pageable, principal);
    }
```