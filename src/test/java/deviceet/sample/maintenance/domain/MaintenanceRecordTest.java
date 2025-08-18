package deviceet.sample.maintenance.domain;

import deviceet.common.model.operator.UserOperator;
import org.junit.jupiter.api.Test;

import static deviceet.RandomTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MaintenanceRecordTest {

    @Test
    void should_create_maintenance_record() {
        UserOperator principal = randomUserPrincipal();

        MaintenanceRecord maintenanceRecord = new MaintenanceRecord("equipment",
                "name",
                randomEquipmentStatus(),
                randomDescription(),
                principal);

        assertNotNull(maintenanceRecord.getId());
    }
}
