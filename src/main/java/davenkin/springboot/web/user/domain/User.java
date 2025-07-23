package davenkin.springboot.web.user.domain;

import davenkin.springboot.web.common.model.AggregateRoot;
import davenkin.springboot.web.user.domain.event.UserCreatedEvent;
import davenkin.springboot.web.user.domain.event.UserNameUpdatedEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import static davenkin.springboot.web.common.Constants.USER_COLLECTION;
import static davenkin.springboot.web.common.model.AggregateRootType.USER;
import static davenkin.springboot.web.common.utils.SnowflakeIdGenerator.newSnowflakeId;
import static lombok.AccessLevel.PRIVATE;

@Getter
@FieldNameConstants
@Document(USER_COLLECTION)
@TypeAlias("USER")
@NoArgsConstructor(access = PRIVATE)
public class User extends AggregateRoot {

    private String name;

    public User(String name) {
        super(newUserId(), USER);
        this.name = name;
        raiseEvent(new UserCreatedEvent(name, this.getId()));
    }

    public static String newUserId() {
        return "USR" + newSnowflakeId();
    }

    public void updateName(String newName) {
        UserNameUpdatedEvent userNameUpdatedEvent = new UserNameUpdatedEvent(name, newName, this.getId());
        this.name = newName;
        raiseEvent(userNameUpdatedEvent);
    }
}
