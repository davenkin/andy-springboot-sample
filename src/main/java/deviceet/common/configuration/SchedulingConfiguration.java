package deviceet.common.configuration;

import deviceet.common.configuration.profile.NonCiProfile;
import deviceet.common.event.publish.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@NonCiProfile
@Configuration
@RequiredArgsConstructor
@EnableSchedulerLock(defaultLockAtMostFor = "60m", defaultLockAtLeastFor = "10s")
public class SchedulingConfiguration {
    private final DomainEventPublisher domainEventPublisher;

    @Scheduled(cron = "0 */1 * * * ?")
    public void houseKeepPublishStagedDomainEvents() {
        log.debug("Start house keep publish domain events.");
        domainEventPublisher.publishStagedDomainEvents();
    }

    //todo: 删除发送事件和接收时间，每个月删除3个月之前的
}
