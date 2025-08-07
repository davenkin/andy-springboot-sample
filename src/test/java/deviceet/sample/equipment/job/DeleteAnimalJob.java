package deviceet.sample.equipment.job;

import deviceet.sample.equipment.job.task.DeleteAllAnimalsTask;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class DeleteAnimalJob {
    private final DeleteAllAnimalsTask deleteAllAnimalsTask;

    public void deleteAllAnimals() {
        log.info("Start delete all animals.");
        this.deleteAllAnimalsTask.run();
        log.info("End delete all animals.");
    }
}
