package deviceet.user.domain;

import deviceet.common.model.AbstractEntity;
import deviceet.user.domain.event.UserCreatedEvent;
import deviceet.user.domain.event.UserNameUpdatedEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

import static deviceet.common.utils.Constants.PLATFORM_ORG_ID;
import static deviceet.common.utils.Constants.USER_COLLECTION;
import static deviceet.common.utils.SnowflakeIdGenerator.newSnowflakeId;
import static lombok.AccessLevel.PRIVATE;

@Getter
@TypeAlias("USER")
@FieldNameConstants
@Document(USER_COLLECTION)
@NoArgsConstructor(access = PRIVATE)
public class User extends AbstractEntity {

    private String name;

    public User(String name) {
        super(newUserId(), PLATFORM_ORG_ID);//PLATFORM_ORG_ID传进来
        this.name = name;
        raiseEvent(new UserCreatedEvent(this));
    }

    public static String newUserId() {
        return "USR" + newSnowflakeId();
    }

    public void updateName(String newName) {
        if (Objects.equals(newName, this.name)) {
            return;
        }

        UserNameUpdatedEvent userNameUpdatedEvent = new UserNameUpdatedEvent(name, newName, this);
        this.name = newName;
        raiseEvent(userNameUpdatedEvent);
    }
}
