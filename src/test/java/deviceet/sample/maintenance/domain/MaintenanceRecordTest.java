package deviceet.sample.maintenance.domain;

import deviceet.common.model.principal.UserPrincipal;
import org.junit.jupiter.api.Test;

import static deviceet.RandomTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MaintenanceRecordTest {

    @Test
    void should_create_maintenance_record() {
        UserPrincipal principal = randomUserPrincipal();

        MaintenanceRecord maintenanceRecord = new MaintenanceRecord("equipment",
                "name",
                randomEquipmentStatus(),
                randomDescription(),
                principal);

        assertNotNull(maintenanceRecord.getId());
    }
}
