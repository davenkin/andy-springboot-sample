package deviceet.common.configuration;

import deviceet.common.configuration.profile.DisableForCI;
import deviceet.common.event.DomainEventJobs;
import deviceet.common.event.publish.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@DisableForCI
@Configuration
@EnableScheduling
@RequiredArgsConstructor
@EnableSchedulerLock(defaultLockAtMostFor = "60m", defaultLockAtLeastFor = "10s")
public class SchedulingConfiguration {
    private final DomainEventPublisher domainEventPublisher;
    private final DomainEventJobs domainEventJobs;

    //This job should not use @SchedulerLock as DomainEventPublisher.publishStagedDomainEvents() already uses an internal distributed lock
    @Scheduled(cron = "0 */1 * * * ?")
    public void houseKeepPublishStagedDomainEvents() {
        log.debug("Start house keep publish domain events.");
        domainEventPublisher.publishStagedDomainEvents();
    }

    @Scheduled(cron = "0 10 2 1 * ?")
    @SchedulerLock(name = "removeOldDomainEvents", lockAtMostFor = "60m", lockAtLeastFor = "1m")
    public void removeOldDomainEvents() {
        try {
            domainEventJobs.removeOldPublishingDomainEventsFromMongo(100);
        } catch (Throwable t) {
            log.error("Failed remove old publishing domain events from mongo.", t);
        }

        try {
            domainEventJobs.removeOldConsumingDomainEventsFromMongo(100);
        } catch (Throwable t) {
            log.error("Failed remove old consuming domain events from mongo.", t);
        }
    }
}
