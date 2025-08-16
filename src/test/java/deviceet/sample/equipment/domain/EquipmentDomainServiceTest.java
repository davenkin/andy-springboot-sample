package deviceet.sample.equipment.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static deviceet.RandomTestUtils.randomUserPrincipal;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class EquipmentDomainServiceTest {

    @Mock
    private EquipmentRepository equipmentRepository;

    @InjectMocks
    private EquipmentDomainService equipmentDomainService;

    @Test
    void shouldUpdateName() {
        Mockito.when(equipmentRepository.existsByName(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        Equipment equipment = new Equipment("name", randomUserPrincipal());

        equipmentDomainService.updateEquipmentName(equipment, "newName");

        assertEquals("newName", equipment.getName());
    }
}