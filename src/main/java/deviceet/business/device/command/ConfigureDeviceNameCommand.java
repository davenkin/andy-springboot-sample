package deviceet.business.device.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ConfigureDeviceNameCommand(@NotBlank @Size(max = 100) String name) {
}
