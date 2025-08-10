# Common coding practices

- Do not rely on database to generate IDs, instead, generate IDs within the code. This means when the object is created,
  its ID should already been generated. Reason: This decouples our code from database implementations.

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

- Prefer using Java Record over Lombok for value objects. Reason: Records Java built in support, and are more concise
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

- Use Spring Data's default pagination mechanism. This means the following query parameters should be used:
    - `page`: the zero-based page index
    - `size`: the page size
    - `sort`: for sorting, format is `sort=abc,desc`, where `abc` is the field to be sorted, `desc` means descending and
      `asc` means ascending.
    - Also the following configuration is applied for responding a stable `Page` object.

  ```java
  @EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
  public class CommonConfiguration {}
  ```