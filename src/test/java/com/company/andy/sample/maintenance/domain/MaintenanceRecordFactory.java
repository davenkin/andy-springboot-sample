package com.company.andy.sample.maintenance.domain;

import com.company.andy.common.model.operator.Operator;
import com.company.andy.sample.equipment.domain.Equipment;
import com.company.andy.sample.equipment.domain.EquipmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MaintenanceRecordFactory {

    public MaintenanceRecord create(Equipment equipment,
                                    EquipmentStatus status,
                                    String description,
                                    Operator operator) {
        return new MaintenanceRecord(equipment.getId(), equipment.getName(), status, description, operator);
    }
}
