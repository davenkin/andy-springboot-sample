package deviceet.sample.equipment.domain;

import deviceet.TestUtils;
import deviceet.common.model.Principal;
import org.junit.jupiter.api.Test;

import static deviceet.common.event.DomainEventType.EQUIPMENT_CREATED_EVENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EquipmentTest {
    @Test
    void shouldCreateEquipment() {
        Principal principal = TestUtils.randomPrincipal();
        Equipment equipment = new Equipment("name", principal);
        assertEquals("name", equipment.getName());
        assertEquals(1, equipment.getEvents().size());
        assertTrue(equipment.getEvents().stream()
                .anyMatch(domainEvent -> domainEvent.getType() == EQUIPMENT_CREATED_EVENT));
    }
}
