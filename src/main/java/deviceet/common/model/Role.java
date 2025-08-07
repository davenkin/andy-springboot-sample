package deviceet.common.model;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public enum Role {
    ORG_IT_ADMIN,
    ORG_ADMIN,
    ROOT;

    public Set<Role> effectiveRoles() {
        return Stream.concat(Set.of(this).stream(), this.impliedRoles.stream()).collect(toSet());
    }
}
