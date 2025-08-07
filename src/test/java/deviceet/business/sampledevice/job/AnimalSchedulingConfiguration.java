package deviceet.business.sampledevice.job;

import deviceet.common.configuration.profile.DisableForIT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

// This class serves as an example of schedulers to call jobs
@Slf4j
@DisableForIT
@EnableScheduling
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableSchedulerLock(defaultLockAtMostFor = "60m", defaultLockAtLeastFor = "10s")
public class AnimalSchedulingConfiguration {
    private final DeleteAnimalJob deleteAnimalJob;

    @Scheduled(cron = "0 10 2 1 * ?")
    @SchedulerLock(name = "deleteAllAnimals", lockAtMostFor = "60m", lockAtLeastFor = "1m")
    public void deleteAllAnimals() {
        this.deleteAnimalJob.deleteAllAnimals();
    }
}
