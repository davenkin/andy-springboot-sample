package deviceet.common.event.consume;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static deviceet.common.utils.CommonUtils.singleParameterizedArgumentClassOf;
import static java.util.Comparator.comparingInt;

// todo: added test, e.g. multiple handlers can handle the same events independently
@Slf4j
@Component
public class EventConsumer<T> {
    private final Map<String, Class<?>> handlerToEventMapping = new ConcurrentHashMap<>();
    private final List<AbstractEventHandler<T>> handlers;
    private final ConsumingEventDao<T> consumingEventDao;
    private final TransactionTemplate transactionTemplate;
    private final RetryTemplate retryTemplate;

    public EventConsumer(List<AbstractEventHandler<T>> handlers,
                         ConsumingEventDao<T> consumingEventDao,
                         PlatformTransactionManager transactionManager) {
        this.handlers = handlers;
        this.consumingEventDao = consumingEventDao;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.retryTemplate = this.buildRetryTemplate();
    }

    public void consume(ConsumingEvent<T> event) {
        if (event == null) {
            return;
        }

        log.debug("Start consume event[{}:{}].", event.getType(), event.getEventId());
        this.handlers.stream()
                .filter(handler -> canHandle(handler, event.getEvent()))
                .sorted(comparingInt(AbstractEventHandler::priority))
                .forEach(handler -> {
                    try {
                        retryTemplate.execute(
                                context -> {
                                    if (handler.isTransactional()) {
                                        this.transactionTemplate.execute(status -> {
                                            recordAndHandle(handler, event);
                                            return null;
                                        });
                                    } else {
                                        recordAndHandle(handler, event);
                                    }
                                    return null;
                                }
                        );
                    } catch (Throwable t) {
                        log.error("Error occurred while handling event[{}:{}] by {}.",
                                event.getType(), event.getEventId(), handler.getName(), t);
                    }
                });
    }

    private void recordAndHandle(AbstractEventHandler<T> handler, ConsumingEvent<T> consumingEvent) {
        if (handler.isIdempotent() || this.consumingEventDao.recordAsConsumed(consumingEvent, handler)) {
            handler.handle(consumingEvent.getEvent());
        } else {
            log.warn("Event[{}:{}] has already been consumed by handler[{}], skip handling.",
                    consumingEvent.getEventId(), consumingEvent.getType(), handler.getName());
        }
    }

    private boolean canHandle(AbstractEventHandler<T> handler, T event) {
        String handlerClassName = handler.getName();

        if (!this.handlerToEventMapping.containsKey(handlerClassName)) {
            Class<?> handlerEventClass = singleParameterizedArgumentClassOf(handler.getClass());
            this.handlerToEventMapping.put(handlerClassName, handlerEventClass);
        }

        Class<?> finalHandlerEventClass = this.handlerToEventMapping.get(handlerClassName);
        return finalHandlerEventClass != null && finalHandlerEventClass.isAssignableFrom(event.getClass());
    }

    private RetryTemplate buildRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(500);
        backOffPolicy.setMultiplier(2.0);

        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(new SimpleRetryPolicy(2));
        return retryTemplate;
    }
}
