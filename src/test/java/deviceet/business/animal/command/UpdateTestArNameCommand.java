package deviceet.business.animal.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateTestArNameCommand(@NotBlank @Size(max = 100) String name) {
}
