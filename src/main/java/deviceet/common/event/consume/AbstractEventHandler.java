package deviceet.common.event.consume;

import static deviceet.common.utils.CommonUtils.singleParameterizedArgumentClassOf;

// All event handler should extend this class
public abstract class AbstractEventHandler<T> {
    private final Class<?> eventClass;

    protected AbstractEventHandler() {
        this.eventClass = singleParameterizedArgumentClassOf(this.getClass());
    }

    public boolean isIdempotent() {
        return false; // By default, all handlers are assumed to be not idempotent by themselves
    }

    public boolean isTransactional() {
        return true; // By default, all handlers are assumed to be transactional, we should make as many handlers as possible to be transactional
    }

    public int priority() {
        return 0; // Smaller value means higher priority and will be handled first
    }

    public final String getName() {
        return this.getClass().getName();
    }

    public final boolean canHandle(T event) {
        return this.eventClass.isAssignableFrom(event.getClass());
    }

    public abstract void handle(T event);
}
