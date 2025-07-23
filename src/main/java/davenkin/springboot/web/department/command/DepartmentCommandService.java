package davenkin.springboot.web.department.command;

import davenkin.springboot.web.department.domain.Department;
import davenkin.springboot.web.department.domain.DepartmentFactory;
import davenkin.springboot.web.department.domain.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DepartmentCommandService {
    private final DepartmentFactory departmentFactory;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public String createDepartment(CreateDepartmentCommand command) {
        Department department = this.departmentFactory.createDepartment(command.name(), command.creator());
        this.departmentRepository.save(department);
        log.info("Created department {}.", department.getId());
        return department.getId();
    }

}
