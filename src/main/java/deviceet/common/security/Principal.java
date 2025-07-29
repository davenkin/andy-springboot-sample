package deviceet.common.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Set;

import static deviceet.common.exception.ServiceException.accessDeniedException;
import static deviceet.common.security.Role.ROOT;
import static deviceet.common.utils.CommonUtils.requireNonBlank;
import static deviceet.common.utils.Constants.PLATFORM_TENANT_ID;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public class Principal {
    private final String tenantId;
    private final String userId;
    private final String userName;
    private final Set<Role> roles;

    private Principal(String tenantId, String userId, String userName, Role role) {
        requireNonBlank(tenantId, "tenantId must not be blank.");
        requireNonBlank(userId, "userId must not be blank.");
        requireNonBlank(userName, "userName must not be blank.");
        requireNonNull(role, "role must not be null.");

        this.tenantId = tenantId;
        this.userId = userId;
        this.userName = userName;
        this.roles = role.effectiveRoles();
    }

    public static Principal rootPrincipal(String userId, String userName) {
        return new Principal(PLATFORM_TENANT_ID, userId, userName, ROOT);
    }

    public static Principal tenantPrincipal(String tenantId, String userId, String userName, Role role) {
        return new Principal(tenantId, userId, userName, role);
    }

    //todo: add tenantPrincipalFromUser(User user)

    public boolean isRoot() {
        return this.roles.contains(ROOT);
    }

    public boolean hasRole(Role role) {
        return this.roles.contains(role);
    }

    public void checkHasRole(Role role) {
        if (!this.hasRole(role)) {
            throw accessDeniedException();
        }
    }

}
