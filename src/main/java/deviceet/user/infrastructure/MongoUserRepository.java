package deviceet.user.infrastructure;

import deviceet.common.infrastructure.AbstractMongoRepository;
import deviceet.user.domain.CachedOrgUser;
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
    public void save(User entity) {
        super.save(entity);
        this.cachedMongoUserRepository.evictCachedOrgUsers(entity.getOrgId());
    }

    @Override
    public void save(List<User> entities) {
        if (isEmpty(entities)) {
            return;
        }

        super.save(entities);
        this.cachedMongoUserRepository.evictCachedOrgUsers(entities.get(0).getOrgId());
    }

    @Override
    public void delete(User entity) {
        super.delete(entity);
        this.cachedMongoUserRepository.evictCachedOrgUsers(entity.getOrgId());
    }

    @Override
    public void delete(List<User> entities) {
        if (isEmpty(entities)) {
            return;
        }

        super.delete(entities);
        this.cachedMongoUserRepository.evictCachedOrgUsers(entities.get(0).getOrgId());
    }

    @Override
    public List<CachedOrgUser> cachedOrgUsers(String orgId) {
        requireNonBlank(orgId, "orgId must not be blank.");

        return cachedMongoUserRepository.cachedOrgUsers(orgId).users();
    }
}
