package com.company.andy.common.infrastructure;

import com.company.andy.IntegrationTest;
import com.company.andy.common.event.DomainEventType;
import com.company.andy.common.event.publish.PublishingDomainEventDao;
import com.company.andy.common.model.operator.Operator;
import com.company.andy.sample.equipment.command.CreateEquipmentCommand;
import com.company.andy.sample.equipment.command.EquipmentCommandService;
import com.company.andy.sample.equipment.domain.EquipmentRepository;
import com.company.andy.sample.equipment.domain.event.EquipmentNameUpdatedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static com.company.andy.RandomTestUtils.*;
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
        Operator operator = randomUserOperator();
        CreateEquipmentCommand createEquipmentCommand = randomCreateEquipmentCommand();
        String equipmentId = equipmentCommandService.createEquipment(createEquipmentCommand, operator);
        doThrow(new RuntimeException("stub exception")).when(publishingDomainEventDao).stage(anyList());

        assertThrows(RuntimeException.class, () -> equipmentCommandService.updateEquipmentName(equipmentId, randomUpdateEquipmentNameCommand(), operator));

        assertEquals(createEquipmentCommand.name(), equipmentRepository.byId(equipmentId).getName());
        assertNull(latestEventFor(equipmentId, DomainEventType.EQUIPMENT_NAME_UPDATED_EVENT, EquipmentNameUpdatedEvent.class));
    }

}