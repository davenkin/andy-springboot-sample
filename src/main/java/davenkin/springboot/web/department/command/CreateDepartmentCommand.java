package davenkin.springboot.web.department.command;

import davenkin.springboot.web.user.domain.UserReference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

// In our example, we use the passed in creator just for demo
// In real project, the creator usually is fetched from the current authenticated principle(user)

@Builder
public record CreateDepartmentCommand(@NotBlank @Size(max = 100) String name, UserReference creator) {
}
