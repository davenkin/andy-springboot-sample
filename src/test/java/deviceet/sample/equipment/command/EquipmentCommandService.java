package deviceet.sample.equipment.command;

import deviceet.common.model.principal.Operator;
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
    public String createEquipment(CreateEquipmentCommand command, Operator operator) {
        Equipment equipment = equipmentFactory.create(command.name(), operator);
        equipmentRepository.save(equipment);
        log.info("Created Equipment[{}].", equipment.getId());
        return equipment.getId();
    }

    @Transactional
    public void updateEquipmentName(String id, UpdateEquipmentNameCommand command, Operator operator) {
        Equipment equipment = equipmentRepository.byId(id, operator.getOrgId());
        equipmentDomainService.updateEquipmentName(equipment, command.name());
        equipmentRepository.save(equipment);
        log.info("Updated name for Equipment[{}].", equipment.getId());
    }

    @Transactional
    public void updateEquipmentHolder(String id, UpdateEquipmentHolderCommand command, Operator operator) {
        Equipment equipment = equipmentRepository.byId(id, operator.getOrgId());
        equipment.updateHolder(command.name());
        equipmentRepository.save(equipment);
        log.info("Updated holder for Equipment[{}].", equipment.getId());
    }

    @Transactional
    public void deleteEquipment(String equipmentId, Operator operator) {
        Equipment equipment = equipmentRepository.byId(equipmentId, operator.getOrgId());
        equipmentRepository.delete(equipment);
        log.info("Deleted Equipment[{}].", equipmentId);
    }
}
