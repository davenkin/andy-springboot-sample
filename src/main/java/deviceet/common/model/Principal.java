package deviceet.common.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Set;

import static deviceet.common.model.Role.ROOT;
import static deviceet.common.utils.CommonUtils.requireNonBlank;
import static java.util.Objects.requireNonNull;

// Principal should contain enough context data of the current user, feel free to modify according to your own needs

@Getter
@EqualsAndHashCode
public class Principal {
    public static final String ROBOT_USER_ID = "Robot001";
    public static final String ROBOT_USER_NAME = "Mr.Robot";
    public static final String ROBOT_ORG_ID = "Robots";
    public static final Principal ROBOT = new Principal(ROBOT_USER_ID, ROBOT_USER_NAME, ROOT, ROBOT_ORG_ID);

    private final String userId;
    private final String userName;
    private final Set<Role> roles;
    private final String orgId;

    public Principal(String userId, String userName, Role role, String orgId) {
        requireNonBlank(userId, "userId must not be blank.");
        requireNonBlank(userName, "userName must not be blank.");
        requireNonNull(role, "role must not be null.");
        requireNonBlank(orgId, "orgId must not be blank.");

        this.orgId = orgId;
        this.userId = userId;
        this.userName = userName;
        this.roles = Set.of(role);
    }

    public static Principal robotForOrg(String orgId) {
        requireNonBlank(orgId, "orgId must not be blank.");
        return new Principal(ROBOT_USER_ID, ROBOT_USER_NAME, ROOT, orgId);
    }

    public static Principal robot() {
        return ROBOT;
    }


    @Override
    public String toString() {
        return "Principal{" +
               "userId='" + userId + '\'' +
               ", userName='" + userName + '\'' +
               ", orgId='" + orgId + '\'' +
               '}';
    }
}
