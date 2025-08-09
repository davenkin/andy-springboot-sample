package deviceet.common.model.principal;

import lombok.EqualsAndHashCode;

import java.util.Set;

import static deviceet.common.util.CommonUtils.requireNonBlank;
import static java.util.Objects.requireNonNull;

// Represents a human user principal, used by CommandService and QueryService for human user interaction
@EqualsAndHashCode
public class UserPrincipal implements Principal {
    private final String id;
    private final String name;
    private final Set<Role> roles;
    private final String orgId;

    private UserPrincipal(String id, String name, Role role, String orgId) {
        requireNonBlank(id, "id must not be blank.");
        requireNonBlank(name, "name must not be blank.");
        requireNonNull(role, "role must not be null.");
        requireNonBlank(orgId, "orgId must not be blank.");

        this.orgId = orgId;
        this.id = id;
        this.name = name;
        this.roles = Set.of(role);
    }

    public static UserPrincipal of(String id, String name, Role role, String orgId) {
        return new UserPrincipal(id, name, role, orgId);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<Role> getRoles() {
        return roles;
    }

    @Override
    public String getOrgId() {
        return orgId;
    }
}
