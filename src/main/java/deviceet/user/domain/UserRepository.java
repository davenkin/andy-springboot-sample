package deviceet.user.domain;

import java.util.List;

public interface UserRepository {
    void save(User user);

    User byId(String id, String orgId);

    List<CachedOrgUser> cachedOrgUsers(String orgId);
}
