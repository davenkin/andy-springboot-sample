package davenkin.springboot.web.department.domain;


import davenkin.springboot.web.user.domain.UserReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DepartmentFactory {

    public Department createDepartment(String name, UserReference creator) {
        return new Department(name, creator);
    }
}
