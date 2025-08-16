package deviceet.common.event.publish;

import deviceet.common.event.DomainEvent;

import java.util.concurrent.CompletableFuture;

// Send a domain event to the messaging middleware
public interface DomainEventSender {
    CompletableFuture<String> send(DomainEvent domainEvent);
}
