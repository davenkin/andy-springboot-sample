package deviceet.common.infrastructure;

import deviceet.IntegrationTest;
import deviceet.common.event.DomainEventType;
import deviceet.common.event.publish.PublishingDomainEventDao;
import deviceet.common.model.principal.Operator;
import deviceet.sample.equipment.command.CreateEquipmentCommand;
import deviceet.sample.equipment.command.EquipmentCommandService;
import deviceet.sample.equipment.domain.EquipmentRepository;
import deviceet.sample.equipment.domain.event.EquipmentNameUpdatedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static deviceet.RandomTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;

class TransactionIntegrationTest extends IntegrationTest {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @MockitoSpyBean
    private PublishingDomainEventDao publishingDomainEventDao;

    @Autowired
    private EquipmentCommandService equipmentCommandService;

    @Test
    void transaction_should_work() {
        Operator operator = randomUserPrincipal();
        CreateEquipmentCommand createEquipmentCommand = randomCreateEquipmentCommand();
        String equipmentId = equipmentCommandService.createEquipment(createEquipmentCommand, operator);
        doThrow(new RuntimeException("stub exception")).when(publishingDomainEventDao).stage(anyList());

        assertThrows(RuntimeException.class, () -> equipmentCommandService.updateEquipmentName(equipmentId, randomUpdateEquipmentNameCommand(), operator));

        assertEquals(createEquipmentCommand.name(), equipmentRepository.byId(equipmentId).getName());
        assertNull(latestEventFor(equipmentId, DomainEventType.EQUIPMENT_NAME_UPDATED_EVENT, EquipmentNameUpdatedEvent.class));
    }

}