package com.company.andy.sample.equipment.domain;

import com.company.andy.common.exception.ServiceException;
import com.company.andy.common.model.AggregateRoot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.company.andy.common.exception.ErrorCode.EQUIPMENT_NAME_ALREADY_EXISTS;
import static com.company.andy.common.util.NullableMapUtils.mapOf;

@Component
@RequiredArgsConstructor
public class EquipmentDomainService {
    private final EquipmentRepository equipmentRepository;

    public void updateEquipmentName(Equipment equipment, String newName) {
        if (!Objects.equals(newName, equipment.getName()) &&
            equipmentRepository.existsByName(newName, equipment.getOrgId())) {
            throw new ServiceException(EQUIPMENT_NAME_ALREADY_EXISTS,
                    "Equipment Name Already Exists.",
                    mapOf(AggregateRoot.Fields.id, equipment.getId(), Equipment.Fields.name, newName));
        }

        equipment.updateName(newName);
    }
}
