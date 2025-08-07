package deviceet.sample.equipment.eventhandler;

import deviceet.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class EquipmentCreatedEventHandlerIntegrationTest extends IntegrationTest {
    @Autowired
    private EquipmentCreatedEventHandler equipmentCreatedEventHandler;

    @Test
    void test() {
    }
}
