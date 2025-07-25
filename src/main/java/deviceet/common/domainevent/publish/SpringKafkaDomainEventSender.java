package deviceet.common.domainevent.publish;

import deviceet.common.configuration.profile.NonCiProfile;
import deviceet.common.domainevent.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

// Send domain events to Kafka
// This is the only place where event publishing touches Kafka, hence the coupling to Kafka is minimised
@NonCiProfile
@Slf4j
@Component
@RequiredArgsConstructor
public class SpringKafkaDomainEventSender implements DomainEventSender {
    private static final String KAFKA_DOMAIN_EVENT_TOPIC_SUFFIX = "_domain_event";
    private final KafkaTemplate<String, DomainEvent> kafkaTemplate;

    @Override
    public CompletableFuture<String> send(DomainEvent event) {
        String topic = event.getArType().name().toLowerCase() + KAFKA_DOMAIN_EVENT_TOPIC_SUFFIX;
        return this.kafkaTemplate.send(topic, event.getArId(), event)
                .thenApply(record -> record.getProducerRecord().value().getId());
    }
}
