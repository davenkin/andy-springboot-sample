package davenkin.springboot.web.common.domainevent.consume;

import davenkin.springboot.web.common.configuration.profile.NonCiProfile;
import davenkin.springboot.web.common.domainevent.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

// Receive domain events from Kafka
// This is the only place where event consuming touches Kafka, hence the coupling to Kafka is minimised

// Normally we use @NonBuildProfile to disable it for build pipeline
@NonCiProfile
@Slf4j
@Component
@RequiredArgsConstructor
public class SpringKafkaDomainEventListener {
    private final DomainEventConsumer<DomainEvent> domainEventConsumer;

    @KafkaListener(id = "domain-event-listener",
            groupId = "changeme",
            topics = {"user_domain_event", "department_domain_event"})
    public void listen(DomainEvent event) {
        this.domainEventConsumer.consume(new ConsumingDomainEvent<>(event.getId(), event.getType().name(), event));
    }
}
