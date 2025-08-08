package deviceet.sample.equipment.query;

import deviceet.sample.equipment.domain.EquipmentStatus;
import lombok.Builder;

@Builder
public record ListEquipmentQuery(String search, EquipmentStatus status) {
}

