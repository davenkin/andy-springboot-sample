package deviceet.common.model.principal;

import java.util.Set;

// Principal should contain enough context data of the current user, feel free to modify according to your own needs

public interface Principal {
    String getId();

    String getName();

    Set<Role> getRoles();

    String getOrgId();
}
