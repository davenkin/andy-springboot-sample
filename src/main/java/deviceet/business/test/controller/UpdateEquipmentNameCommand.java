package deviceet.business.test.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateEquipmentNameCommand(@NotBlank @Size(max = 100) String name) {
}
