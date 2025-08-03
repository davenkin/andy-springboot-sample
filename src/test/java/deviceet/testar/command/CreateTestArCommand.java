package deviceet.testar.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateTestArCommand(@NotBlank @Size(max = 100) String name) {
}
