package deviceet.sample.equipment.command;

import deviceet.common.model.principal.Principal;
import deviceet.sample.equipment.domain.Equipment;
import deviceet.sample.equipment.domain.EquipmentDomainService;
import deviceet.sample.equipment.domain.EquipmentFactory;
import deviceet.sample.equipment.domain.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class EquipmentCommandService {
    private final EquipmentRepository equipmentRepository;
    private final EquipmentFactory equipmentFactory;
    private final EquipmentDomainService equipmentDomainService;

    @Transactional
    public String createEquipment(CreateEquipmentCommand command, Principal principal) {
        Equipment equipment = equipmentFactory.create(command.name(), principal);
        equipmentRepository.save(equipment);
        log.info("Created Equipment[{}].", equipment.getId());
        return equipment.getId();
    }

    @Transactional
    public void updateEquipmentName(String id, UpdateEquipmentNameCommand command, Principal principal) {
        Equipment equipment = equipmentRepository.byId(id, principal.getOrgId());
        equipmentDomainService.updateEquipmentName(equipment, command.name());
        equipmentRepository.save(equipment);
        log.info("Updated name for Equipment[{}].", equipment.getId());
    }

    @Transactional
    public void updateEquipmentHolder(String id, UpdateEquipmentHolderCommand command, Principal principal) {
        Equipment equipment = equipmentRepository.byId(id, principal.getOrgId());
        equipment.updateHolder(command.name());
        equipmentRepository.save(equipment);
        log.info("Updated holder for Equipment[{}].", equipment.getId());
    }

    @Transactional
    public void deleteEquipment(String equipmentId, Principal principal) {
        Equipment equipment = equipmentRepository.byId(equipmentId, principal.getOrgId());
        equipmentRepository.delete(equipment);
        log.info("Deleted Equipment[{}].", equipmentId);
    }
}
