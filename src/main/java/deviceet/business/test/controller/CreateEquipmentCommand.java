package deviceet.business.test.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateEquipmentCommand(
        @Schema(description = "Name of the equipment")
        @NotBlank @Size(max = 100) String name) {
}
