package deviceet.sample.maintenance;

import deviceet.sample.equipment.domain.EquipmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateMaintenanceRecordCommand(
        @NotBlank String equipmentId,
        @NotBlank @Size(max = 1000) String description,
        @NotNull EquipmentStatus equipmentStatus
) {
}
