package deviceet.common.model.principal;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

import static deviceet.common.model.principal.Role.PLATFORM;
import static lombok.AccessLevel.PRIVATE;

// Represents a non human principal, used in non human interaction scenarios like EventHandlers and background Jobs

@EqualsAndHashCode
@NoArgsConstructor(access = PRIVATE)
public class PlatformPrincipal implements Principal {
    public static final PlatformPrincipal PLATFORM_PRINCIPAL = new PlatformPrincipal();
    public static final String PLATFORM_ID = "PLATFORM001";
    public static final String PLATFORM_NAME = "PLATFORM";

    @Override
    public String getId() {
        return PLATFORM_ID;
    }

    @Override
    public String getName() {
        return PLATFORM_NAME;
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
