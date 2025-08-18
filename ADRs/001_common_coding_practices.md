# Common coding practices

- Do not rely on database to generate IDs, instead, generate IDs within the code
  using [SnowflakeIdGenerator.newSnowflakeId()](../src/main/java/deviceet/common/util/SnowflakeIdGenerator.java). This
  means when the object is created,
  its ID should already been generated. Reason: This decouples the code from database implementations and also makes
  testing much easier.

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

- All pagination request should extend [PageableRequest](../src/main/java/deviceet/common/util/PageableRequest.java),
  which has the following pagination fields:
    - `pageNumber`: the zero-based page index
    - `pageSize`: the page size
    - `pageSort`: list of sorting fields, each with format `abc,desc`, where `abc` is the field to be sorted, `desc`
      means descending and `asc` means ascending. The first field is sorted first, then the second.
    - The following annotations are required on all pagination requests:
        - `@Getter`: for getter field values
        - `@SuperBuilder`: for builder
        - `@EqualsAndHashCode(callSuper = true)`: for equals() and hashcode()
        - `@NoArgsConstructor(access = PRIVATE)`: for Json deserialization

Example pagination
request [ListEquipmentsQuery](../src/test/java/deviceet/sample/equipment/query/ListEquipmentsQuery.java):

```java
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = PRIVATE)
public class ListEquipmentsQuery extends PageableRequest {
    @Schema(description = "Search text")
    @Max(50)
    private String search;

    @Schema(description = "Equipment status to query")
    private EquipmentStatus status;
}
```

- All pagination response should return [PagedResponse](../src/main/java/deviceet/common/util/PagedResponse.java).

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
if (listEquipmentsQuery.status() != null) {

   // Use "Equipment.Fields.status" to access Equipment's "status" field
   criteria.and(Equipment.Fields.status).is(listEquipmentsQuery.status());
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
    public String createEquipment(CreateEquipmentCommand command, Operator operator) {
        Equipment equipment = equipmentFactory.create(command.name(), operator);
        equipmentRepository.save(equipment);
        log.info("Created Equipment[{}].", equipment.getId());
        return equipment.getId();
    }
```

- If distributed lock is required, used
  Shedlock's [LockingTaskExecutor](../src/main/java/deviceet/common/configuration/DistributedLockConfiguration.java).
- Use HTTP POST for all pagination request, put all query fields into a `Query` object even if there is only one field.
  Reason: a `Query` object wraps multiple fields together that's easy to pass around.
  Example: use [ListEquipmentsQuery](../src/test/java/deviceet/sample/equipment/query/ListEquipmentsQuery.java) to query
  multiple equipments.

```java
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = PRIVATE)
public class ListEquipmentsQuery extends PageableRequest {
    @Schema(description = "Search text")
    @Max(50)
    private String search;

    @Schema(description = "Equipment status to query")
    private EquipmentStatus status;
}
```

The controller receives a `ListEquipmentsQuery` object using POST method:

```java
    @Operation(summary = "Query equipments")
    @PostMapping("/list")
    public PagedResponse<QListedEquipment> listEquipments(@RequestBody @Valid ListEquipmentsQuery query) {
        // In real situations, operator is normally created from the current user in context, such as Spring Security's SecurityContextHolder
        Operator operator = SAMPLE_USER_OPERATOR;

        return this.equipmentQueryService.listEquipments(query, operator);
    }
```

- Make configuration files, e.g. `application.yaml` as simple as possible, prefer using constants in the code.
- Do not create interface classes for services until really needed. The public methods on service classes already serve
  as interfaces.