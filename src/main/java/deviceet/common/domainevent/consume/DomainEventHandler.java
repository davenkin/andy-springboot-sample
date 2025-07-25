package deviceet.common.domainevent.consume;

public interface DomainEventHandler<T> {

    default boolean isIdempotent() {
        return false;
    }

    default int order() {
        return 100;
    }

    void handle(ConsumingDomainEvent<T> consumingDomainEvent);
}
