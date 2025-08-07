package deviceet.sample.equipment.query;

import lombok.Builder;

@Builder
public record ListEquipmentQuery(String search) {
}

