package deviceet.common.security;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public enum Role {
    ORG_IT_ADMIN(Set.of()),
    ORG_ADMIN(Set.of(ORG_IT_ADMIN)),
    ROOT(Set.of(ORG_IT_ADMIN, ORG_ADMIN));

    private final Set<Role> impliedRoles;

    Role(Set<Role> impliedRoles) {
        this.impliedRoles = impliedRoles;
    }

    public Set<Role> effectiveRoles() {
        return Stream.concat(Set.of(this).stream(), this.impliedRoles.stream()).collect(toSet());
    }
}
