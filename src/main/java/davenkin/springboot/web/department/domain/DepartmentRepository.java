package davenkin.springboot.web.department.domain;

public interface DepartmentRepository {
    void save(Department department);

    Department byId(String id);

    void updateAllDepartmentsCreatorName(String userId, String userName);

}
