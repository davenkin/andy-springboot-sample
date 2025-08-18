package deviceet.sample.equipment.command;

import deviceet.IntegrationTest;
import deviceet.common.model.operator.Operator;
import deviceet.sample.equipment.domain.Equipment;
import deviceet.sample.equipment.domain.EquipmentRepository;
import deviceet.sample.equipment.domain.event.EquipmentCreatedEvent;
import deviceet.sample.equipment.domain.event.EquipmentNameUpdatedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static deviceet.RandomTestUtils.*;
import static deviceet.common.event.DomainEventType.EQUIPMENT_CREATED_EVENT;
import static deviceet.common.event.DomainEventType.EQUIPMENT_NAME_UPDATED_EVENT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EquipmentCommandServiceIntegrationTest extends IntegrationTest {
    @Autowired
    private EquipmentCommandService equipmentCommandService;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Test
    void should_create_equipment() {
        Operator operator = randomUserOperator();

        CreateEquipmentCommand createEquipmentCommand = randomCreateEquipmentCommand();
        String equipmentId = equipmentCommandService.createEquipment(createEquipmentCommand, operator);

        Equipment equipment = equipmentRepository.byId(equipmentId);
        assertEquals(createEquipmentCommand.name(), equipment.getName());
        assertEquals(operator.getOrgId(), equipment.getOrgId());

        // Only need to check the existence of domain event in database,
        // no need to further test event handler as that will be addressed in event handlers' own tests
        EquipmentCreatedEvent equipmentCreatedEvent = latestEventFor(equipmentId, EQUIPMENT_CREATED_EVENT, EquipmentCreatedEvent.class);
        assertEquals(equipmentId, equipmentCreatedEvent.getEquipmentId());
    }

    @Test
    void should_update_equipment_name() {
        Operator operator = randomUserOperator();

        CreateEquipmentCommand createEquipmentCommand = randomCreateEquipmentCommand();
        String equipmentId = equipmentCommandService.createEquipment(createEquipmentCommand, operator);

        UpdateEquipmentNameCommand updateEquipmentNameCommand = randomUpdateEquipmentNameCommand();
        equipmentCommandService.updateEquipmentName(equipmentId, updateEquipmentNameCommand, operator);

        Equipment equipment = equipmentRepository.byId(equipmentId);
        assertEquals(updateEquipmentNameCommand.name(), equipment.getName());
        EquipmentNameUpdatedEvent equipmentNameUpdatedEvent = latestEventFor(equipmentId, EQUIPMENT_NAME_UPDATED_EVENT, EquipmentNameUpdatedEvent.class);
        assertEquals(equipmentId, equipmentNameUpdatedEvent.getEquipmentId());
        assertEquals(updateEquipmentNameCommand.name(), equipmentNameUpdatedEvent.getUpdatedName());
    }
}
