package deviceet.common.event.consume.infrastructure;

import deviceet.common.configuration.profile.DisableForIT;
import deviceet.common.event.DomainEvent;
import deviceet.common.event.consume.EventConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static deviceet.common.utils.Constants.KAFKA_DOMAIN_EVENT_TOPIC;

// Entry point for receiving events from Kafka
// This is the only place where event consuming touches Kafka, hence the coupling to Kafka is minimised

@Slf4j
@Component
@DisableForIT // Disable Kafka for integration test
@RequiredArgsConstructor
public class SpringKafkaEventListener {
    private final EventConsumer eventConsumer;

    @KafkaListener(id = "domain-event-listener",
            groupId = "domain-event-listener",
            topics = {KAFKA_DOMAIN_EVENT_TOPIC},
            concurrency = "3")
    public void listenDomainEvent(DomainEvent event) {
        this.eventConsumer.consumeDomainEvent(event);
    }

    // add more listener

}
