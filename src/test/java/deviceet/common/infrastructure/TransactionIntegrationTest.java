package deviceet.common.infrastructure;

import deviceet.IntegrationTest;
import deviceet.common.event.DomainEventType;
import deviceet.common.event.publish.PublishingDomainEventDao;
import deviceet.common.model.principal.Principal;
import deviceet.sample.equipment.command.CreateEquipmentCommand;
import deviceet.sample.equipment.command.EquipmentCommandService;
import deviceet.sample.equipment.domain.EquipmentFactory;
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
    private EquipmentFactory equipmentFactory;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @MockitoSpyBean
    private PublishingDomainEventDao publishingDomainEventDao;

    @Autowired
    private EquipmentCommandService equipmentCommandService;

    @Test
    void transaction_should_work() {
        Principal principal = randomUserPrincipal();
        CreateEquipmentCommand createEquipmentCommand = randomCreateEquipmentCommand();
        String equipmentId = equipmentCommandService.createEquipment(createEquipmentCommand, principal);
        doThrow(new RuntimeException("stub exception")).when(publishingDomainEventDao).stage(anyList());

        assertThrows(RuntimeException.class, () -> equipmentCommandService.updateEquipmentName(equipmentId, randomUpdateEquipmentNameCommand(), principal));

        assertEquals(createEquipmentCommand.name(), equipmentRepository.byId(equipmentId).getName());
        assertNull(latestEventFor(equipmentId, DomainEventType.EQUIPMENT_NAME_UPDATED_EVENT, EquipmentNameUpdatedEvent.class));
    }

}