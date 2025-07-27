package deviceet.user.infrastructure;

import deviceet.common.infrastructure.AbstractMongoRepository;
import deviceet.user.domain.CachedTenantUser;
import deviceet.user.domain.User;
import deviceet.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static deviceet.common.utils.CommonUtils.requireNonBlank;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Repository
@RequiredArgsConstructor
public class MongoUserRepository extends AbstractMongoRepository<User> implements UserRepository {
    private final CachedMongoUserRepository cachedMongoUserRepository;

    @Override
    public void save(User user) {
        super.save(user);
        this.cachedMongoUserRepository.evictCachedTenantUsers(user.getTenantId());
    }

    @Override
    public void save(List<User> users) {
        if (isEmpty(users)) {
            return;
        }

        super.save(users);
        this.cachedMongoUserRepository.evictCachedTenantUsers(users.get(0).getTenantId());
    }

    @Override
    public void delete(User user) {
        super.delete(user);
        this.cachedMongoUserRepository.evictCachedTenantUsers(user.getTenantId());
    }

    @Override
    public void delete(List<User> users) {
        if (isEmpty(users)) {
            return;
        }

        super.delete(users);
        this.cachedMongoUserRepository.evictCachedTenantUsers(users.get(0).getTenantId());
    }

    @Override
    public List<CachedTenantUser> cachedTenantUsers(String tenantId) {
        requireNonBlank(tenantId, "Tenant ID must not be blank.");

        return cachedMongoUserRepository.cachedTenantUsers(tenantId).users();
    }
}
