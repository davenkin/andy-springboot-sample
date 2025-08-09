package deviceet.common.model.principal;

import lombok.EqualsAndHashCode;

import java.util.Set;

import static deviceet.common.model.principal.Role.PLATFORM;

// Represents a non human principal, used in non human interaction scenarios like EventHandlers and background Jobs

@EqualsAndHashCode
public class PlatformPrincipal implements Principal {
    public static final PlatformPrincipal PLATFORM_PRINCIPAL = new PlatformPrincipal();
    public static final String PLATFORM_ID = "PLATFORM";

    private PlatformPrincipal() {
    }

    @Override
    public String getId() {
        return PLATFORM_ID;
    }

    @Override
    public String getName() {
        return PLATFORM_ID;
    }

    @Override
    public Set<Role> getRoles() {
        return Set.of(PLATFORM);
    }

    @Override
    public String getOrgId() {
        throw new IllegalArgumentException("PlatformPrincipal does not support getOrgId().");
    }
}
