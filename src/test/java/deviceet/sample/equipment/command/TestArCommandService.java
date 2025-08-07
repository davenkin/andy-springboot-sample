package deviceet.sample.equipment.command;

import deviceet.common.model.Principal;
import deviceet.sample.equipment.domain.Equipment;
import deviceet.sample.equipment.domain.EquipmentFactory;
import deviceet.sample.equipment.domain.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestArCommandService {
    private final EquipmentRepository equipmentRepository;
    private final EquipmentFactory equipmentFactory;

    @Transactional
    public String createTestAr(CreateTestArCommand command, Principal principal) {
        Equipment equipment = equipmentFactory.create(command.name(), principal);
        equipmentRepository.save(equipment);
        log.info("Created TestAr[{}].", equipment.getId());
        return equipment.getId();
    }

    @Transactional
    public void updateTestArName(String id, UpdateTestArNameCommand command, Principal principal) {
        Equipment equipment = equipmentRepository.byId(id, principal.getOrgId());
        equipment.updateName(command.name());
        equipmentRepository.save(equipment);
        log.info("Updated name for TestAr[{}].", equipment.getId());
    }
}
