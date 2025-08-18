package deviceet.sample.equipment.domain;

import deviceet.RandomTestUtils;
import deviceet.common.model.principal.Operator;
import org.junit.jupiter.api.Test;

import static deviceet.common.event.DomainEventType.EQUIPMENT_CREATED_EVENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EquipmentTest {
    @Test
    void shouldCreateEquipment() {
        Operator operator = RandomTestUtils.randomUserPrincipal();
        Equipment equipment = new Equipment("name", operator);
        assertEquals("name", equipment.getName());
        assertEquals(1, equipment.getEvents().size());
        assertTrue(equipment.getEvents().stream()
                .anyMatch(domainEvent -> domainEvent.getType() == EQUIPMENT_CREATED_EVENT));
    }
}
