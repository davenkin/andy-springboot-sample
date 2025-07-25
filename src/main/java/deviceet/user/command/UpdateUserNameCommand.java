package deviceet.user.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateUserNameCommand(@NotBlank @Size(max = 100) String name) {
}
