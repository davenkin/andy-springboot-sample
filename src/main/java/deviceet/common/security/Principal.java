package deviceet.common.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Set;

import static deviceet.common.exception.ServiceException.accessDeniedException;
import static deviceet.common.security.Role.ROOT;
import static deviceet.common.utils.CommonUtils.requireNonBlank;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public class Principal {
    private final String userId;
    private final String userName;
    private final Set<Role> roles;
    private final String orgId;

    public Principal(String userId, String userName, Role role, String orgId) {
        requireNonBlank(orgId, "orgId must not be blank.");
        requireNonBlank(userId, "userId must not be blank.");
        requireNonBlank(userName, "userName must not be blank.");
        requireNonNull(role, "role must not be null.");

        this.orgId = orgId;
        this.userId = userId;
        this.userName = userName;
        this.roles = role.effectiveRoles();
    }

    public boolean isRoot() {
        return this.roles.contains(ROOT);
    }

    public boolean hasRole(Role role) {
        return this.roles.contains(role);
    }

    public void checkRole(Role role) {
        if (!this.hasRole(role)) {
            throw accessDeniedException();
        }
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
