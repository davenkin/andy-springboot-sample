package deviceet.sample.equipment.query;

import deviceet.sample.equipment.domain.EquipmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import lombok.Builder;

@Builder
public record ListEquipmentQuery(
        @Schema(description = "Search text")
        @Max(50)
        String search,
        @Schema(description = "Equipment status to query")
        EquipmentStatus status) {
}

