package davenkin.springboot.web.department.infrastructure;

import davenkin.springboot.web.common.infrastructure.MongoBaseRepository;
import davenkin.springboot.web.department.domain.Department;
import davenkin.springboot.web.department.domain.DepartmentRepository;
import davenkin.springboot.web.user.domain.UserReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import static davenkin.springboot.web.common.utils.CommonUtils.joinMongoFields;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Slf4j
@Repository
public class DefaultDepartmentRepository extends MongoBaseRepository<Department> implements DepartmentRepository {
    @Override
    public void updateAllDepartmentsCreatorName(String userId, String userName) {
        Query query = Query.query(where(joinMongoFields(Department.Fields.creator, UserReference.Fields.id)).is(userId));
        Update update = new Update().set(joinMongoFields(Department.Fields.creator, UserReference.Fields.name), userName);
        this.mongoTemplate.updateMulti(query, update, Department.class);
    }
}
