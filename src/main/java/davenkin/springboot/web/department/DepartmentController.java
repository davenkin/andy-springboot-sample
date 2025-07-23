package davenkin.springboot.web.department;

import davenkin.springboot.web.common.utils.ResponseId;
import davenkin.springboot.web.department.command.CreateDepartmentCommand;
import davenkin.springboot.web.department.command.DepartmentCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/departments")
public class DepartmentController {
    private final DepartmentCommandService departmentCommandService;

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseId createDepartment(@RequestBody @Valid CreateDepartmentCommand command) {
        String userId = this.departmentCommandService.createDepartment(command);
        return new ResponseId(userId);
    }

}
