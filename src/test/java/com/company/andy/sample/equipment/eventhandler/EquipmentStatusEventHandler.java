package com.company.andy.sample.equipment.eventhandler;

import com.company.andy.common.event.consume.AbstractEventHandler;
import com.company.andy.sample.equipment.domain.EquipmentRepository;
import com.company.andy.sample.equipment.domain.event.EquipmentStatusUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EquipmentStatusEventHandler extends AbstractEventHandler<EquipmentStatusUpdatedEvent> {
    private final EquipmentRepository equipmentRepository;

    @Override
    public void handle(EquipmentStatusUpdatedEvent event) {
        equipmentRepository.evictCachedEquipmentSummaries(event.getArOrgId());
    }
}
