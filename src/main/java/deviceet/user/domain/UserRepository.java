package deviceet.user.domain;

import java.util.List;

public interface UserRepository {
    void save(User user);

    User byId(String id, String tenantId);

    List<CachedTenantUser> cachedTenantUsers(String tenantId);
}
