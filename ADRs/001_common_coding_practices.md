## Common coding practices

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

- Prefer using Java Record for value objects over Lombok. Reason: Records are more concise and embodies common best
  practices like immutability.

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
  they does not convey any business logic, making the code hard to read and comprehend.