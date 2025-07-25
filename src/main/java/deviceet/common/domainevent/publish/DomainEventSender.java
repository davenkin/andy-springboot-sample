package deviceet.common.domainevent.publish;

import deviceet.common.domainevent.DomainEvent;

import java.util.concurrent.CompletableFuture;

public interface DomainEventSender {
    CompletableFuture<String> send(DomainEvent domainEvent);
}
