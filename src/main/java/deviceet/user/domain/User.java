package deviceet.user.domain;

import deviceet.common.model.AggregateRoot;
import deviceet.user.domain.event.UserCreatedEvent;
import deviceet.user.domain.event.UserNameUpdatedEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

import static deviceet.common.model.AggregateRootType.USER;
import static deviceet.common.utils.Constants.PLATFORM_TENANT_ID;
import static deviceet.common.utils.Constants.USER_COLLECTION;
import static deviceet.common.utils.SnowflakeIdGenerator.newSnowflakeId;
import static lombok.AccessLevel.PRIVATE;

@Getter
@FieldNameConstants
@Document(USER_COLLECTION)
@TypeAlias("USER")
@NoArgsConstructor(access = PRIVATE)
public class User extends AggregateRoot {

    private String name;

    public User(String name) {
        super(newUserId(), PLATFORM_TENANT_ID, USER);
        this.name = name;
        raiseEvent(new UserCreatedEvent(name, this.getId()));
    }

    public static String newUserId() {
        return "USR" + newSnowflakeId();
    }

    public void updateName(String newName) {
        if (Objects.equals(newName, this.name)) {
            return;
        }

        UserNameUpdatedEvent userNameUpdatedEvent = new UserNameUpdatedEvent(this.getId(), name, newName);
        this.name = newName;
        raiseEvent(userNameUpdatedEvent);
    }
}
