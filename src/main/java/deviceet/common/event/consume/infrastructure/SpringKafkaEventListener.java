package deviceet.common.event.consume.infrastructure;

import deviceet.common.configuration.profile.DisableForCI;
import deviceet.common.event.DomainEvent;
import deviceet.common.event.consume.ConsumingEvent;
import deviceet.common.event.consume.EventConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

// Entry point for receiving events from Kafka
// This is the only place where event consuming touches Kafka, hence the coupling to Kafka is minimised

@Slf4j
@Component
@DisableForCI// Disable Kafka for CI
@RequiredArgsConstructor
public class SpringKafkaEventListener {
    private final EventConsumer<DomainEvent> eventConsumer;

    @KafkaListener(id = "domain-event-listener",
            groupId = "domain-event-listener",
            topics = {"user_domain_event"},
            concurrency = "3")
    public void listen(DomainEvent event) {
        this.eventConsumer.consume(new ConsumingEvent<>(event.getId(), event));
    }

    //you can add more @KafkaListener methods to handle other types of events
}
