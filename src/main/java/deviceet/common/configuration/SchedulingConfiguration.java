package deviceet.common.configuration;

import deviceet.common.configuration.profile.DisableForIT;
import deviceet.common.event.DomainEventHouseKeepingJob;
import deviceet.common.event.publish.DomainEventPublishJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@DisableForIT
@EnableScheduling
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableSchedulerLock(defaultLockAtMostFor = "60m", defaultLockAtLeastFor = "10s")
public class SchedulingConfiguration {
    private final DomainEventPublishJob domainEventPublishJob;
    private final DomainEventHouseKeepingJob domainEventHouseKeepingJob;

    //This job should not use @SchedulerLock as DomainEventPublisher.publishStagedDomainEvents() already uses an internal distributed lock
    @Scheduled(cron = "0 */1 * * * ?")
    public void houseKeepPublishStagedDomainEvents() {
        log.debug("Start house keep publish domain events.");
        domainEventPublishJob.publishStagedDomainEvents(100);
    }

    @Scheduled(cron = "0 10 2 1 * ?")
    @SchedulerLock(name = "removeOldDomainEvents", lockAtMostFor = "60m", lockAtLeastFor = "1m")
    public void removeOldDomainEvents() {
        try {
            domainEventHouseKeepingJob.removeOldPublishingDomainEventsFromMongo(100);
        } catch (Throwable t) {
            log.error("Failed remove old publishing domain events from mongo.", t);
        }

        try {
            domainEventHouseKeepingJob.removeOldConsumingDomainEventsFromMongo(100);
        } catch (Throwable t) {
            log.error("Failed remove old consuming domain events from mongo.", t);
        }
    }
}
