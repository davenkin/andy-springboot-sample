package davenkin.springboot.web.common.domainevent.publish;

import davenkin.springboot.web.common.domainevent.DomainEvent;

import java.util.concurrent.CompletableFuture;

public interface DomainEventSender {
    CompletableFuture<String> send(DomainEvent domainEvent);
}
