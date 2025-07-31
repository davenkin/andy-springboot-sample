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
    public void save(User ar) {
        super.save(ar);
        this.cachedMongoUserRepository.evictCachedOrgUsers(ar.getOrgId());
    }

    @Override
    public void save(List<User> ars) {
        if (isEmpty(ars)) {
            return;
        }

        super.save(ars);
        this.cachedMongoUserRepository.evictCachedOrgUsers(ars.get(0).getOrgId());
    }

    @Override
    public void delete(User ar) {
        super.delete(ar);
        this.cachedMongoUserRepository.evictCachedOrgUsers(ar.getOrgId());
    }

    @Override
    public void delete(List<User> ars) {
        if (isEmpty(ars)) {
            return;
        }

        super.delete(ars);
        this.cachedMongoUserRepository.evictCachedOrgUsers(ars.get(0).getOrgId());
    }

    @Override
    public List<CachedOrgUser> cachedOrgUsers(String orgId) {
        requireNonBlank(orgId, "orgId must not be blank.");

        return cachedMongoUserRepository.cachedOrgUsers(orgId).users();
    }
}
