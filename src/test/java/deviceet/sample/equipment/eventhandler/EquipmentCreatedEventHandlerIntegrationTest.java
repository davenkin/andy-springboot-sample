package deviceet.sample.equipment.eventhandler;

import deviceet.IntegrationTest;
import deviceet.RandomTestUtils;
import deviceet.common.model.operator.Operator;
import deviceet.sample.equipment.command.CreateEquipmentCommand;
import deviceet.sample.equipment.command.EquipmentCommandService;
import deviceet.sample.equipment.domain.EquipmentRepository;
import deviceet.sample.equipment.domain.EquipmentSummary;
import deviceet.sample.equipment.domain.event.EquipmentCreatedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static deviceet.common.event.DomainEventType.EQUIPMENT_CREATED_EVENT;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EquipmentCreatedEventHandlerIntegrationTest extends IntegrationTest {
    @Autowired
    private EquipmentCreatedEventHandler equipmentCreatedEventHandler;

    @Autowired
    private EquipmentCommandService equipmentCommandService;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Test
    void should_evict_org_equipment_summaries_cache() {
        Operator operator = RandomTestUtils.randomUserOperator();
        CreateEquipmentCommand createEquipmentCommand = new CreateEquipmentCommand(RandomTestUtils.randomEquipmentName());
        String equipmentId = equipmentCommandService.createEquipment(createEquipmentCommand, operator);
        String cacheKey = "Cache:ORG_EQUIPMENTS::" + operator.getOrgId();
        assertFalse(stringRedisTemplate.hasKey(cacheKey));
        List<EquipmentSummary> equipmentSummaries = equipmentRepository.cachedEquipmentSummaries(operator.getOrgId());
        assertTrue(stringRedisTemplate.hasKey(cacheKey));
        EquipmentCreatedEvent equipmentCreatedEvent = latestEventFor(equipmentId, EQUIPMENT_CREATED_EVENT, EquipmentCreatedEvent.class);

        equipmentCreatedEventHandler.handle(equipmentCreatedEvent);
        assertFalse(stringRedisTemplate.hasKey(cacheKey));
    }
}
