package deviceet.business.testar.job;

import deviceet.business.testar.job.task.DeleteAllTestArsTask;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class TestArJob {
    private final DeleteAllTestArsTask deleteAllTestArsTask;

    public void deleteAllTestArs() {
        log.info("Start delete all test ars.");
        this.deleteAllTestArsTask.run();
        log.info("End delete all test ars.");
    }
}
