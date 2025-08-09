package deviceet.sample.equipment.job;

import deviceet.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MaintenanceReminderJobIntegrationTest extends IntegrationTest {
    @Autowired
    private MaintenanceReminderJob maintenanceReminderJob;

    @Test
    void should_run_maintenance_reminder_job() {
        maintenanceReminderJob.run();
    }
}
