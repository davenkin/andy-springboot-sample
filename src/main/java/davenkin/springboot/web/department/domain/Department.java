package davenkin.springboot.web.department.domain;

import davenkin.springboot.web.common.model.AggregateRoot;
import davenkin.springboot.web.department.domain.event.DepartmentCreatedEvent;
import davenkin.springboot.web.user.domain.UserReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import static deviceet.common.Constants.DEPARTMENT_COLLECTION;
import static davenkin.springboot.web.common.model.AggregateRootType.DEPARTMENT;
import static deviceet.common.utils.SnowflakeIdGenerator.newSnowflakeId;
import static lombok.AccessLevel.PRIVATE;

@Getter
@FieldNameConstants
@Document(DEPARTMENT_COLLECTION)
@TypeAlias("DEPARTMENT")
@NoArgsConstructor(access = PRIVATE)
public class Department extends AggregateRoot {

    private String name;
    private UserReference creator;

    public Department(String name, UserReference creator) {
        super(newDepartmentId(), DEPARTMENT);
        this.name = name;
        this.creator = creator;
        this.raiseEvent(new DepartmentCreatedEvent(name, this.getId()));
    }

    public static String newDepartmentId() {
        return "DPT" + newSnowflakeId();
    }

}
