package deviceet.common.event.publish.infrastructure;

import deviceet.common.configuration.profile.EnableForIT;
import deviceet.common.event.DomainEvent;
import deviceet.common.event.publish.DomainEventSender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Getter
@Component
@EnableForIT
@RequiredArgsConstructor
public class FakeDomainEventSender implements DomainEventSender {
    private final Map<String, DomainEvent> events = new HashMap<>();

    @Override
    public CompletableFuture<String> send(DomainEvent event) {
        this.events.put(event.getId(), event);
        return CompletableFuture.completedFuture(event.getId());
    }

}
