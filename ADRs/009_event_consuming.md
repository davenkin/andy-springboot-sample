# Event consuming

## Context

Event consuming can be as easy as writing a listener method annotated with `@KafkaListener`, or it can be quite hard
given issues like ordering, idempotency, retry and dead letter handling etc.

## Decision

We choose to follow these strategies when consuming events:

- Every event can be handled by multiple handlers, these handlers process the same event independently from each other,
  exceptions from one handler will not impact other handlers
- For multiple handlers processing the same event, higher priority handler process the event earlier
- Upon exceptions, the event will be retried 3 times within the same consuming thread, if retry exhausts, the
  event will be put into Dead Letter Topic(DLT), there is no automatic listener on DLT hence human investigation and
  action is needed
- Event consuming idempotency is achieved by either:
    - making the handler itself idempotent
    - or using `consuming-event` table to record consumed events, this prevents events from being consumed a second time
      by the same handler, hence idempotency is achieved
- Event handlers can be configured to work within a transaction

## Implementation

- When consuming an event, the only thing from your side is to create an event handler class that
  extends [AbstractEventHandler](../src/main/java/deviceet/common/event/consume/AbstractEventHandler.java), and make
  sure the event's topic is subscribed
  by [SpringKafkaEventListener](../src/main/java/deviceet/common/event/consume/infrastructure/SpringKafkaEventListener.java)
- Example event handler:

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

- When extends [AbstractEventHandler](../src/main/java/deviceet/common/event/consume/AbstractEventHandler.java), you may
  override the following methods:
    - `isIdempotent()`: Returns `true` if the handler itself is idempotent, if `false` is returned, `consuming-event`
      table will be used to achieve idempotency
    - `isTransactional()`: Returns `true` to put the handler inside a transaction
    - If `isIdempotent()` return `false` and `isTransactional()` return `true`, both the `consuming-event` table and the
      handler will be put inside the same transaction
    - `priority()`: return a number indicates the priority of the handler, lower number means higher priority and will
      be process earlier if multiple handlers consume the same event

## Event consuming infrastructure
====

- Do not change the name and location of event handler classes after they are created, otherwise the event consuming
  mechanism
  might not work as expected because the event idempotency is based on event handlers' full class names, so choose
  carefully at the beginning.

- One event can be consumed by multiple event handlers independently.
- For EventHandler, do not rename or change package after created, because we rely on the fully qualified class name(
  FQCN) for checking if an event is already handled by a handler